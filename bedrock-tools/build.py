#!/usr/bin/env python3
"""
Builds the Bedrock "Classic Baby Mobs" resource pack.

For every baby mob changed by Tiny Takeover (Bedrock 26.10) it merges the vanilla client entity file
from 26.0 (the last version before the update) with the current one (26.60):

  * everything from the current file is kept and gated with "!v.cbm_classic"
  * everything from the 26.0 file is added under renamed keys ("cbm_...") and gated with "v.cbm_classic"
  * render controllers / animation controllers / animations / geometry from 26.0 that no longer
    exist unchanged in the current pack are copied in under new ids ending in ".cbm_classic"

v.cbm_classic is true for babies whose classic look is switched on. In the "settings" pack it reads the
pack settings (one toggle per mob); in the "simple" pack it is simply query.is_baby.
"""
import copy, json, os, re, shutil, subprocess, sys, uuid
sys.path.insert(0, os.path.dirname(__file__))
from common import strip_comments

REPO = '/home/claude/ref/bedrock'
OLD_TAG, NEW_TAG = 'v1.26.0.2', 'v1.26.60.5'
OLD = f'/home/claude/ref/bed-{OLD_TAG}/resource_pack'
NEW = f'/home/claude/ref/bed-{NEW_TAG}/resource_pack'
NS = 'classic_baby_mobs'
SUFFIX = '.cbm_classic'

# (entity file, setting id, label shown in the settings screen, group)
MOBS = [
    ('chicken', 'chicken', 'Chick', 'Farm Animals'),
    ('cow', 'cow', 'Calf', 'Farm Animals'),
    ('mooshroom', 'mooshroom', 'Mooshroom Calf', 'Farm Animals'),
    ('pig', 'pig', 'Piglet', 'Farm Animals'),
    ('sheep', 'sheep', 'Lamb', 'Farm Animals'),
    ('goat', 'goat', 'Goat Kid', 'Farm Animals'),
    ('rabbit', 'rabbit', 'Baby Rabbit', 'Farm Animals'),
    ('horse_v3', 'horse', 'Foal (Horse)', 'Mounts'),
    ('donkey_v3', 'donkey', 'Baby Donkey', 'Mounts'),
    ('mule_v3', 'mule', 'Baby Mule', 'Mounts'),
    ('skeleton_horse_v3', 'skeleton_horse', 'Baby Skeleton Horse', 'Mounts'),
    ('zombie_horse_v3', 'zombie_horse', 'Baby Zombie Horse', 'Mounts'),
    ('camel', 'camel', 'Baby Camel', 'Mounts'),
    ('llama', 'llama', 'Baby Llama', 'Mounts'),
    ('trader_llama', 'trader_llama', 'Baby Trader Llama', 'Mounts'),
    ('strider', 'strider', 'Baby Strider', 'Mounts'),
    ('cat', 'cat', 'Kitten', 'Pets'),
    ('ocelot', 'ocelot', 'Baby Ocelot', 'Pets'),
    ('wolf', 'wolf', 'Puppy', 'Pets'),
    ('fox', 'fox', 'Fox Kit', 'Pets'),
    ('armadillo', 'armadillo', 'Baby Armadillo', 'Wild Animals'),
    ('bee', 'bee', 'Baby Bee', 'Wild Animals'),
    ('panda', 'panda', 'Panda Cub', 'Wild Animals'),
    ('polar_bear', 'polar_bear', 'Polar Bear Cub', 'Wild Animals'),
    ('sniffer', 'sniffer', 'Snifflet', 'Wild Animals'),
    ('axolotl', 'axolotl', 'Baby Axolotl', 'Water Mobs'),
    ('dolphin', 'dolphin', 'Baby Dolphin', 'Water Mobs'),
    ('squid', 'squid', 'Baby Squid', 'Water Mobs'),
    ('glow_squid', 'glow_squid', 'Baby Glow Squid', 'Water Mobs'),
    ('turtle', 'turtle', 'Baby Turtle', 'Water Mobs'),
    ('hoglin', 'hoglin', 'Hoglet', 'Nether Mobs'),
    ('zoglin', 'zoglin', 'Baby Zoglin', 'Nether Mobs'),
    ('piglin', 'piglin', 'Baby Piglin', 'Nether Mobs'),
    ('zombie_pigman', 'zombified_piglin', 'Baby Zombified Piglin', 'Nether Mobs'),
    ('villager_v2', 'villager', 'Baby Villager', 'Villagers & Zombies'),
    ('zombie', 'zombie', 'Baby Zombie', 'Villagers & Zombies'),
    ('husk', 'husk', 'Baby Husk', 'Villagers & Zombies'),
    ('drowned', 'drowned', 'Baby Drowned', 'Villagers & Zombies'),
    ('zombie_villager_v2', 'zombie_villager', 'Baby Zombie Villager', 'Villagers & Zombies'),
]


def read_json(path):
    with open(path, encoding='utf-8-sig') as f:
        return json.loads(strip_comments(f.read()))


def index_defs(root):
    """id -> definition for render controllers, animations, animation controllers and geometry."""
    rcs, anims, acs, geos = {}, {}, {}, {}
    for sub, key, target in (('render_controllers', 'render_controllers', rcs),
                             ('animations', 'animations', anims),
                             ('animation_controllers', 'animation_controllers', acs)):
        d = os.path.join(root, sub)
        for fn in sorted(os.listdir(d)):
            if not fn.endswith('.json'):
                continue
            try:
                data = read_json(os.path.join(d, fn))
            except Exception as e:  # a few vanilla files are not strict JSON
                print('  ! could not parse', sub, fn, e)
                continue
            for k, v in data.get(key, {}).items():
                target[k] = (v, data.get('format_version', '1.8.0'))
    gd = os.path.join(root, 'models', 'entity')
    for fn in sorted(os.listdir(gd)):
        if not fn.endswith('.json'):
            continue
        try:
            data = read_json(os.path.join(gd, fn))
        except Exception as e:
            print('  ! could not parse geometry', fn, e)
            continue
        if 'minecraft:geometry' in data:
            for g in data['minecraft:geometry']:
                gid = g['description']['identifier']
                geos[gid] = ('new', g, data.get('format_version'))
        else:
            for k, v in data.items():
                if k.startswith('geometry.'):
                    gid, _, parent = k.partition(':')
                    geos[gid] = ('old', (k, v), data.get('format_version'))
    return rcs, anims, acs, geos


def texture_files(tag):
    out = subprocess.run(['git', '-C', REPO, 'ls-tree', '-r', '--name-only', tag, 'resource_pack/textures/'],
                         capture_output=True, text=True, check=True).stdout.split('\n')
    files = {}
    for line in out:
        if not line:
            continue
        rel = line[len('resource_pack/'):]
        base, ext = os.path.splitext(rel)
        if ext in ('.png', '.tga', '.jpg'):
            files.setdefault(base, []).append(line)
    return files


class Builder:
    def __init__(self, out_dir, simple):
        self.out = out_dir
        self.simple = simple
        self.old_rc, self.old_anim, self.old_ac, self.old_geo = OLD_IDX
        self.new_rc, self.new_anim, self.new_ac, self.new_geo = NEW_IDX
        self.copied_rc, self.copied_anim, self.copied_ac, self.copied_geo = {}, {}, {}, {}
        self.copied_textures = set()
        self.report = []

    # ---- helpers -------------------------------------------------------------------------
    def classic_expr(self, setting):
        if self.simple:
            return 'query.is_baby'
        return (f"query.is_baby && query.is_pack_setting_enabled('{NS}:enabled')"
                f" && query.is_pack_setting_enabled('{NS}:{setting}')")

    @staticmethod
    def gate(entry, cond):
        """Turn an animate / render_controllers list entry into one that only runs when cond holds."""
        if isinstance(entry, str):
            return {entry: cond}
        (name, c), = entry.items()
        # In "animate" lists the value is a blend weight (e.g. query.modified_move_speed), so the gate
        # multiplies instead of using && (Molang booleans are 1.0 / 0.0).
        return {name: f'({cond}) * ({c})'}

    def use_old_anim(self, aid):
        """Return the id to use for an animation that the 26.0 entity referenced."""
        if aid in self.new_anim and self.new_anim[aid] == self.old_anim.get(aid):
            return aid
        if aid not in self.old_anim:
            return aid  # built-in or missing; leave as is
        nid = aid + SUFFIX
        self.copied_anim[nid] = self.old_anim[aid]
        return nid

    def use_old_ac(self, acid, renamed_keys):
        """Animation controllers are always copied, with their animation names switched to the cbm_ keys."""
        if acid not in self.old_ac:
            return acid
        nid = acid + SUFFIX
        if nid in self.copied_ac:
            return nid
        definition, fmt = copy.deepcopy(self.old_ac[acid])
        for state in definition.get('states', {}).values():
            new_list = []
            for a in state.get('animations', []):
                if isinstance(a, str):
                    new_list.append(renamed_keys.get(a, a))
                else:
                    (n, c), = a.items()
                    new_list.append({renamed_keys.get(n, n): c})
            if 'animations' in state:
                state['animations'] = new_list
        self.copied_ac[nid] = (definition, fmt)
        return nid

    def use_old_geo(self, gid):
        if gid in self.new_geo and self.new_geo[gid] == self.old_geo.get(gid):
            return gid
        if gid not in self.old_geo:
            self.report.append(f'geometry {gid} not found in 26.0 index')
            return gid
        nid = gid + SUFFIX
        kind, payload, fmt = self.old_geo[gid]
        if kind == 'new':
            g = copy.deepcopy(payload)
            g['description']['identifier'] = nid
            self.copied_geo[nid] = ('new', g, fmt)
        else:
            key, value = payload
            _, _, parent = key.partition(':')
            if parent:
                parent = self.use_old_geo(parent)
                key = nid + ':' + parent
            else:
                key = nid
            self.copied_geo[nid] = ('old', (key, copy.deepcopy(value)), fmt)
        return nid

    def use_old_rc(self, rcid, tex_map, geo_map, mat_map):
        nid = rcid + SUFFIX
        if nid in self.copied_rc:
            return nid
        if rcid not in self.old_rc:
            self.report.append(f'render controller {rcid} not found in 26.0')
            return rcid
        definition, fmt = self.old_rc[rcid]
        text = json.dumps(definition)
        for prefix, mapping in (('Texture', tex_map), ('Geometry', geo_map), ('Material', mat_map)):
            for old_key, new_key in sorted(mapping.items(), key=lambda kv: -len(kv[0])):
                text = re.sub(rf'(?i)\b{prefix}\.{re.escape(old_key)}\b', f'{prefix}.{new_key}', text)
        self.copied_rc[nid] = (json.loads(text), fmt)
        return nid

    def ensure_texture(self, path):
        """Old texture paths that are gone from the current pack are copied in from 26.0."""
        if path in NEW_TEX:
            return
        if path not in OLD_TEX:
            self.report.append(f'texture {path} missing in both versions')
            return
        for src in OLD_TEX[path]:
            rel = src[len('resource_pack/'):]
            dst = os.path.join(self.out, rel)
            os.makedirs(os.path.dirname(dst), exist_ok=True)
            with open(dst, 'wb') as f:
                f.write(subprocess.run(['git', '-C', REPO, 'show', f'{OLD_TAG}:{src}'],
                                       capture_output=True, check=True).stdout)
            self.copied_textures.add(rel)

    # ---- the merge ---------------------------------------------------------------------------
    def merge(self, file, setting):
        old_doc = read_json(os.path.join(OLD, 'entity', f'{file}.entity.json'))
        new_doc = read_json(os.path.join(NEW, 'entity', f'{file}.entity.json'))
        od = old_doc['minecraft:client_entity']['description']
        nd = new_doc['minecraft:client_entity']['description']
        out_doc = copy.deepcopy(new_doc)
        md = out_doc['minecraft:client_entity']['description']
        K = 'v.cbm_classic'
        notK = '!v.cbm_classic'

        # textures, geometry, materials: old keys added as cbm_<key>
        tex_map, geo_map, mat_map = {}, {}, {}
        for section, mapping in (('textures', tex_map), ('geometry', geo_map), ('materials', mat_map)):
            if section not in od:
                continue
            md.setdefault(section, {})
            for k, v in od[section].items():
                nk = 'cbm_' + k
                mapping[k] = nk
                if section == 'geometry':
                    v = self.use_old_geo(v)
                if section == 'textures':
                    self.ensure_texture(v)
                md[section][nk] = v

        # animations: every old entry gets a cbm_ key; changed definitions get copied
        anim_keys = {}
        old_anims = dict(od.get('animations', {}))
        for k in old_anims:
            anim_keys[k] = 'cbm_' + k
        # legacy description-level animation_controllers lists become regular animate entries
        # The engine only runs the legacy description-level "animation_controllers" list when the entity
        # has no scripts.animate (e.g. the old cat); otherwise that list is ignored.
        old_legacy = []
        old_uses_legacy = 'animate' not in od.get('scripts', {})
        for entry in (od.get('animation_controllers', []) if old_uses_legacy else []):
            (n, acid), = entry.items()
            old_anims.setdefault(n, acid)
            anim_keys.setdefault(n, 'cbm_' + n)
            old_legacy.append(n)
        md.setdefault('animations', {})
        for k, aid in old_anims.items():
            if aid.startswith('controller.animation.'):
                md['animations'][anim_keys[k]] = self.use_old_ac(aid, anim_keys)
            else:
                md['animations'][anim_keys[k]] = self.use_old_anim(aid)

        new_legacy = []
        new_uses_legacy = 'animate' not in nd.get('scripts', {})
        for entry in (nd.get('animation_controllers', []) if new_uses_legacy else []):
            (n, acid), = entry.items()
            key = 'nac_' + n
            md['animations'][key] = acid
            new_legacy.append(key)
        md.pop('animation_controllers', None)

        # scripts
        os_ = od.get('scripts', {})
        ns = nd.get('scripts', {})
        ms = md.setdefault('scripts', {})
        cond = self.classic_expr(setting)

        pre = [f'v.cbm_classic = {cond};']
        pre += ns.get('pre_animation', [])
        old_pre = os_.get('pre_animation', [])
        if old_pre and old_pre != ns.get('pre_animation', []):
            pre += ['v.cbm_classic ? {'] + ['  ' + line for line in old_pre] + ['};']
        if any('use_dedicated_baby_armor' in line for line in ns.get('pre_animation', [])):
            # classic babies wear the old (adult-shaped) armor
            pre.append('v.cbm_classic ? { v.use_dedicated_baby_armor = false; };')
        ms['pre_animation'] = pre

        init = list(ns.get('initialize', []))
        for line in os_.get('initialize', []):
            if line not in init:
                init.append(line)
        if init:
            ms['initialize'] = init

        variables = dict(os_.get('variables', {}))
        variables.update(ns.get('variables', {}))
        if variables:
            ms['variables'] = variables

        animate = [self.gate(e, notK) for e in ns.get('animate', [])]
        animate += [{k: notK} for k in new_legacy]
        for e in os_.get('animate', []):
            if isinstance(e, str):
                animate.append({anim_keys.get(e, e): K})
            else:
                (n, c), = e.items()
                animate.append({anim_keys.get(n, n): f'({K}) * ({c})'})
        animate += [{anim_keys[n]: K} for n in old_legacy]
        ms['animate'] = animate

        old_scale = os_.get('scale', '1.0')
        new_scale = ns.get('scale', '1.0')
        if old_scale != new_scale:
            ms['scale'] = f'({cond}) ? ({old_scale}) : ({new_scale})'

        for k, v in os_.items():  # any other old-only script keys
            if k not in ms and k not in ('animate', 'pre_animation', 'initialize', 'variables', 'scale'):
                ms[k] = v

        # render controllers
        rcs = [self.gate(e, notK) for e in nd.get('render_controllers', [])]
        for e in od.get('render_controllers', []):
            if isinstance(e, str):
                rcs.append({self.use_old_rc(e, tex_map, geo_map, mat_map): K})
            else:
                (n, c), = e.items()
                rcs.append({self.use_old_rc(n, tex_map, geo_map, mat_map): f'({K}) * ({c})'})
        md['render_controllers'] = rcs

        # effects used by old controllers
        for section in ('particle_effects', 'sound_effects', 'particle_emitters'):
            for k, v in od.get(section, {}).items():
                md.setdefault(section, {}).setdefault(k, v)

        path = os.path.join(self.out, 'entity', f'{file}.entity.json')
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, 'w') as f:
            json.dump(out_doc, f, indent=2)

    def write_shared(self):
        def dump(path, data):
            os.makedirs(os.path.dirname(path), exist_ok=True)
            with open(path, 'w') as f:
                json.dump(data, f, indent=2)

        dump(os.path.join(self.out, 'render_controllers', 'cbm_classic.render_controllers.json'),
             {'format_version': '1.8.0', 'render_controllers': {k: v for k, (v, _) in sorted(self.copied_rc.items())}})
        by_fmt = {}
        for k, (v, fmt) in sorted(self.copied_anim.items()):
            by_fmt.setdefault(fmt, {})[k] = v
        for i, (fmt, anims) in enumerate(sorted(by_fmt.items())):
            dump(os.path.join(self.out, 'animations', f'cbm_classic_{i}.animation.json'),
                 {'format_version': fmt, 'animations': anims})
        by_fmt = {}
        for k, (v, fmt) in sorted(self.copied_ac.items()):
            by_fmt.setdefault(fmt, {})[k] = v
        for i, (fmt, acs) in enumerate(sorted(by_fmt.items())):
            dump(os.path.join(self.out, 'animation_controllers', f'cbm_classic_{i}.animation_controllers.json'),
                 {'format_version': fmt, 'animation_controllers': acs})
        for nid, (kind, payload, fmt) in sorted(self.copied_geo.items()):
            fname = nid.replace('geometry.', '').replace(SUFFIX, '') + '.cbm_classic.geo.json'
            if kind == 'new':
                data = {'format_version': fmt or '1.12.0', 'minecraft:geometry': [payload]}
            else:
                key, value = payload
                data = {'format_version': fmt or '1.8.0', key: value}
            dump(os.path.join(self.out, 'models', 'entity', fname), data)


def manifest(simple, name, header_uuid, module_uuid, version):
    m = {
        'format_version': 3,
        'header': {
            'name': name,
            'description': ('Brings back the pre-Tiny Takeover baby mobs. Every baby uses its classic look.'
                            if simple else
                            'Brings back the pre-Tiny Takeover baby mobs. Choose per mob with the gear (Settings) button next to the pack.'),
            'uuid': header_uuid,
            'version': version,
            'min_engine_version': '1.26.10',
        },
        'modules': [{'type': 'resources', 'uuid': module_uuid, 'version': version}],
        'metadata': {'authors': ['Classic Baby Mobs'], 'license': 'MIT'},
    }
    if not simple:
        settings = [{'type': 'label', 'text': 'ON = classic (pre-Tiny Takeover) look, OFF = new look.'},
                    {'type': 'toggle', 'name': f'{NS}:enabled', 'text': 'Classic Baby Mobs (master switch)', 'default': True}]
        group = None
        for _, setting, label, grp in MOBS:
            if grp != group:
                settings.append({'type': 'label', 'text': grp})
                group = grp
            settings.append({'type': 'toggle', 'name': f'{NS}:{setting}', 'text': label, 'default': True})
        m['settings'] = settings
    return m


if __name__ == '__main__':
    print('indexing 26.0 ...')
    OLD_IDX = index_defs(OLD)
    print('indexing 26.60 ...')
    NEW_IDX = index_defs(NEW)
    OLD_TEX = texture_files(OLD_TAG)
    NEW_TEX = texture_files(NEW_TAG)

    ids = json.load(open(os.path.join(os.path.dirname(__file__), 'uuids.json')))
    for simple, folder, name in ((False, 'ClassicBabyMobs', 'Classic Baby Mobs'),
                                 (True, 'ClassicBabyMobs_AllClassic', 'Classic Baby Mobs (All Classic)')):
        out = os.path.join('/home/claude/bedrock-build/out', folder)
        shutil.rmtree(out, ignore_errors=True)
        os.makedirs(out)
        b = Builder(out, simple)
        for file, setting, _, _ in MOBS:
            b.merge(file, setting)
        b.write_shared()
        key = 'simple' if simple else 'settings'
        with open(os.path.join(out, 'manifest.json'), 'w') as f:
            json.dump(manifest(simple, name, ids[key]['header'], ids[key]['module'], '1.0.0'), f, indent=2)
        shutil.copy('/home/claude/bedrock-build/pack_icon.png', os.path.join(out, 'pack_icon.png'))
        print(f'{folder}: {len(MOBS)} entities, {len(b.copied_rc)} render controllers, '
              f'{len(b.copied_ac)} animation controllers, {len(b.copied_anim)} animations, '
              f'{len(b.copied_geo)} geometries, {len(b.copied_textures)} textures copied')
        for r in sorted(set(b.report)):
            print('   note:', r)

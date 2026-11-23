import json, os, re, sys
sys.path.insert(0, '/home/claude/bedrock-build')
import build
from common import strip_comments
OUT = sys.argv[1]
NEW_IDX = build.index_defs(build.NEW)
OUT_IDX = build.index_defs_partial = None
def load(p): return json.loads(strip_comments(open(p, encoding='utf-8-sig').read()))
# merge new vanilla defs with our pack's defs
rcs = dict(NEW_IDX[0]); anims = dict(NEW_IDX[1]); acs = dict(NEW_IDX[2]); geos = dict(NEW_IDX[3])
for sub, key, tgt in (('render_controllers','render_controllers',rcs),('animations','animations',anims),('animation_controllers','animation_controllers',acs)):
    d = os.path.join(OUT, sub)
    for fn in os.listdir(d) if os.path.isdir(d) else []:
        for k, v in load(os.path.join(d, fn))[key].items(): tgt[k] = (v, None)
gd = os.path.join(OUT, 'models', 'entity')
for fn in os.listdir(gd):
    data = load(os.path.join(gd, fn))
    for g in data.get('minecraft:geometry', []): geos[g['description']['identifier']] = 1
    for k in data:
        if k.startswith('geometry.'): geos[k.partition(':')[0]] = 1
NEW_TEX = build.texture_files(build.NEW_TAG)
errors = 0
for fn in sorted(os.listdir(os.path.join(OUT, 'entity'))):
    d = load(os.path.join(OUT, 'entity', fn))['minecraft:client_entity']['description']
    errs = []
    tex, geo, mat, an = d.get('textures', {}), d.get('geometry', {}), d.get('materials', {}), d.get('animations', {})
    for k, v in tex.items():
        if v not in NEW_TEX and not os.path.exists(os.path.join(OUT, v + '.png')): errs.append(f'texture path missing {v}')
    for k, v in geo.items():
        if v not in geos: errs.append(f'geometry id missing {v}')
    for k, v in an.items():
        if v.startswith('controller.animation.'):
            if v not in acs: errs.append(f'anim controller missing {v}')
            else:
                for st in acs[v][0].get('states', {}).values():
                    for a in st.get('animations', []):
                        n = a if isinstance(a, str) else next(iter(a))
                        if n not in an: errs.append(f'{v} uses animation key {n} not in entity')
        elif v not in anims: errs.append(f'animation missing {v}')
    for e in d['scripts'].get('animate', []):
        n = e if isinstance(e, str) else next(iter(e))
        if n not in an: errs.append(f'animate key missing {n}')
    for e in d.get('render_controllers', []):
        n = e if isinstance(e, str) else next(iter(e))
        if n not in rcs: errs.append(f'render controller missing {n}'); continue
        text = json.dumps(rcs[n][0])
        for pre, m in (('Texture', tex), ('Geometry', geo), ('Material', mat)):
            for ref in set(re.findall(rf'(?i)\b{pre}\.(\w+)', text)):
                if ref.lower() not in {k.lower() for k in m}: errs.append(f'{n} refs {pre}.{ref} not defined')
    if errs:
        errors += len(errs); print('##', fn); [print('   ', x) for x in sorted(set(errs))]
print('ERRORS:', errors)

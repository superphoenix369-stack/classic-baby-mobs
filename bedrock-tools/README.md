# Bedrock pack generator

`build.py` rebuilds the two Bedrock packs from Mojang's official vanilla samples
(https://github.com/Mojang/bedrock-samples). It merges each baby mob's entity file from 26.0 (before
Tiny Takeover) with the current one, so the old and new looks can be switched per mob.
Update `NEW_TAG` in `build.py` when Bedrock updates. Keep `uuids.json` unchanged so the packs keep their
identity, and raise the version number in `manifest()`.

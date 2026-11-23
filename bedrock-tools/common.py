import json, re, subprocess
REPO='/home/claude/ref/bedrock'
OLD='v1.26.0.2'; NEW='v1.26.60.5'
def git_show(tag, path):
    r=subprocess.run(['git','-C',REPO,'show',f'{tag}:{path}'],capture_output=True)
    return r.stdout.decode('utf-8-sig') if r.returncode==0 else None
def strip_comments(t):
    out=[];i=0;n=len(t);ins=False
    while i<n:
        c=t[i]
        if ins:
            out.append(c)
            if c=='\\': out.append(t[i+1]); i+=2; continue
            if c=='"': ins=False
            i+=1; continue
        if c=='"': ins=True; out.append(c); i+=1; continue
        if t.startswith('//',i):
            j=t.find('\n',i); i = n if j<0 else j; continue
        if t.startswith('/*',i):
            j=t.find('*/',i); i = n if j<0 else j+2; continue
        out.append(c); i+=1
    s=''.join(out)
    s=re.sub(r',(\s*[}\]])',r'\1',s)
    return s
def load(tag,path):
    t=git_show(tag,path)
    return None if t is None else json.loads(strip_comments(t))
def ls(tag, d):
    r=subprocess.run(['git','-C',REPO,'ls-tree','--name-only',f'{tag}',d+'/'],capture_output=True,text=True)
    return [l for l in r.stdout.split('\n') if l]

svnadmin create svnRepo
cd svnRepo/
pathf=file:///home/studs/s368796/svnRepo
svn mkdir ${pathf}/trunk ${pathf}/branches ${pathf}/tags -m "feat: create repo" --username="azat222"
svn checkout ${pathf}/trunk workspace
cd workspace

# r0
cp -rf ~/commits/commit0/* ./
svn add *
svn commit -m "r0" --username="azat222"

# r1
svn copy ${pathf}/trunk/ ${pathf}/branches/feature-1 -m "feature-1" --username="azat222"
svn switch ${pathf}/branches/feature-1
cp -f ~/commits/commit1/* ./
svn add *
svn commit -m "r1" --username="azat222"

# r2
svn switch ${pathf}/trunk
cp -f ~/commits/commit2/* ./
svn add *
svn commit -m "r2" --username="azat222"

# r3
cp -f ~/commits/commit3/* ./
svn add *
svn commit -m "r3" --username="azat222"

# r4
svn switch ${pathf}/branches/feature-1
cp -f ~/commits/commit4/* ./
svn add *
svn commit -m "r4" --username="azat222"

# r5
cp -f ~/commits/commit5/* ./
svn add *
svn commit -m "r5" --username="azat222"

# r6
svn copy ${pathf}/trunk/ ${pathf}/branches/feature-2 -m "feature-2" --username="ulliaka"
svn switch ${pathf}/branches/feature-2
cp -f ~/commits/commit6/* ./
svn add *
svn commit -m "r6" --username="ulliaka"

# r7
svn switch ${pathf}/trunk
cp -f ~/commits/commit7/* ./
svn add *
svn commit -m "r7" --username="azat222"

# r8
cp -f ~/commits/commit8/* ./
svn add *
svn commit -m "r8" --username="azat222"

# r9
cp -f ~/commits/commit9/* ./
svn add *
svn commit -m "r9" --username="azat222"

# r10
svn switch ${pathf}/branches/feature-1
cp -f ~/commits/commit10/* ./
svn add *
svn commit -m "r10" --username="azat222"

# r11
svn switch ${pathf}/trunk
svn merge ${pathf}/branches/feature-1 ${pathf}/trunk --username="azat222"
cp -f ~/commits/commit11/* ./
svn add *
svn commit -m "r11" --username="azat222"

# r12
cp -f ~/commits/commit12/* ./
svn add *
svn commit -m "r12" --username="azat222"

#r13
svn switch ${pathf}/branches/feature-2
cp -f ~/commits/commit13/* ./
svn add *
svn commit -m "r13" --username="ulliaka"

# r14
svn switch ${pathf}/trunk
svn merge ${pathf}/branches/feature-2 ${pathf}/trunk --username="azat222"
cp -f ~/commits/commit14/* ./
svn add *
svn commit -m "r14" --username="azat222"
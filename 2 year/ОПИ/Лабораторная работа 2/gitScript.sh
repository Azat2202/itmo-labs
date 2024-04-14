# u1
# git config --global "user.name" "ulliaka"
# git config --global "user.email" "ulliaka@gmail.com"

# u2
# git config --global "user.name" "azat222"
# git config --global "user.email" "azat222@gmail.com"

git init opiRepo


# 0 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

cp -f ~/commits/commit0/* ./
git add .
git commit -m "r0"


# 1 br2 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout -b "second_brunch"
cp -f ~/commits/commit1/* ./
git add .
git commit -m "r1"


# 2 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "master"
cp -f ~/commits/commit2/* ./
git add .
git commit -m "r2"


# 3 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

cp -f ~/commits/commit3/* ./
git add .
git commit -m "r3"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r3"
# На ветке master
# нечего коммитить, нет изменений в рабочем каталоге


# 4 br2 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "second_brunch"
cp -f ~/commits/commit4/* ./
git add .
git commit -m "r4"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r4"
# На ветке second_brunch
# нечего коммитить, нет изменений в рабочем каталоге


# 5 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "master"
cp -f ~/commits/commit5/* ./
git add .
git commit -m "r5"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r5"
# На ветке master
# нечего коммитить, нет изменений в рабочем каталоге


# 6 br3 u2
git config --global "user.name" "azat222"
git config --global "user.email" "azat222@gmail.com"

git checkout -b "third_branch"
cp -f ~/commits/commit6/* ./
git add .
git commit -m "r6"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r6"
# На ветке third_branch
# нечего коммитить, нет изменений в рабочем каталоге


# 7 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "master"
cp -f ~/commits/commit7/* ./
git add .
git commit -m "r7"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r7"
# На ветке master
# нечего коммитить, нет изменений в рабочем каталоге


# 8 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

cp -f ~/commits/commit8/* ./
git add .
git commit -m "r8"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r8"
# На ветке master
# нечего коммитить, нет изменений в рабочем каталоге


# 9 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

cp -f ~/commits/commit9/* ./
git add .
git commit -m "r9"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r9"
# На ветке master
# нечего коммитить, нет изменений в рабочем каталоге


# 10 br2 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "second_brunch"
cp -f ~/commits/commit10/* ./
git add .
git commit -m "r10"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r10"
# На ветке second_brunch
# нечего коммитить, нет изменений в рабочем каталоге


# 11 br2-1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "master"
git merge "second_branch"

# [s367651@helios ~/opi/opiRepo]$ git merge "second_branch"
# Уже обновлено.

cp -f ~/commits/commit11/* ./
git add .
git commit -m "r11"


# 12 br1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "master"
cp -f ~/commits/commit12/* ./
git add .
git commit -m "r12"


# 13 br3 u2
git config --global "user.name" "azat222"
git config --global "user.email" "azat222@gmail.com"

git checkout "third_branch"
cp -f ~/commits/commit13/* ./
git add .
git commit -m "r13"

# [s367651@helios ~/opi/opiRepo]$ git commit -m "r13"
# На ветке third_branch
# нечего коммитить, нет изменений в рабочем каталоге


# 14 br3-1 u1
git config --global "user.name" "ulliaka"
git config --global "user.email" "ulliaka@gmail.com"

git checkout "master"
git merge "third_branch"

# [s367651@helios ~/opi/opiRepo]$ git merge "third_branch"
# Уже обновлено.

cp -f ~/commits/commit14/* ./
git add .
git commit -m "r14"
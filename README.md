# QLBanVeGaTau - Huong Dan Lam Viec Nhom Bang Git

Tai lieu nay dung cho nhom 4 nguoi de lam viec chung tren GitHub, commit dung quy tac va merge code qua Pull Request.

## 1. Cau truc nhanh duoc dung

- Nhanh chinh: `main`
- Nhanh theo thanh vien:
- `dev/member1-auth-user`
- `dev/member2-ticket-invoice`
- `dev/member3-train-route-schedule`
- `dev/member4-report-promotion-service`

Quy uoc: Moi nguoi lam viec tren nhanh rieng, khong commit truc tiep vao `main`.

## 2. Chia viec cho 4 thanh vien (goi y)

- Thanh vien 1 (`dev/member1-auth-user`): Dang nhap, TaiKhoan, NhanVien, KhachHang
- Thanh vien 2 (`dev/member2-ticket-invoice`): Ban ve, lap hoa don, chi tiet hoa don
- Thanh vien 3 (`dev/member3-train-route-schedule`): TuyenTau, Tau, Toa, ChuyenTau
- Thanh vien 4 (`dev/member4-report-promotion-service`): KhuyenMai, DichVu, thong ke bao cao

Neu thay doi lien quan nhieu phan, tao branch feature moi tu branch ca nhan:
- `feature/member2/payment-refactor`

## 3. Quy tac commit (bat buoc)

Dung Conventional Commits:

- `feat(module): mo ta tinh nang`
- `fix(module): mo ta loi da sua`
- `refactor(module): mo ta toi uu`
- `docs(git): cap nhat huong dan`
- `chore(build): cap nhat cau hinh`

Vi du:
- `feat(ticket): them validate cho chon cho ngoi`
- `fix(invoice): sua loi tinh tong tien sau thue`
- `docs(git): them quy trinh tao pull request`

## 4. Quy trinh lam viec hang ngay

### Buoc 1: Lay code moi nhat

```bash
git checkout main
git pull origin main
```

### Buoc 2: Chuyen sang nhanh ca nhan

```bash
git checkout dev/member1-auth-user
```

### Buoc 3: Lam viec va commit

```bash
git add .
git commit -m "feat(auth): them dang nhap bang username"
```

### Buoc 4: Day nhanh len GitHub

```bash
git push -u origin dev/member1-auth-user
```

## 5. Tao Pull Request de merge code

1. Vao repo GitHub: `https://github.com/lamvyx/QLBanVeGaTau`
2. Chon `Compare & pull request` tu branch cua ban
3. Base: `main`, Compare: branch cua ban
4. Dien template PR (tom tat thay doi, cach test, anh huong)
5. Gan reviewer it nhat 1 thanh vien
6. Chi merge khi:
- Da review xong
- Khong co conflict
- Da test chuc nang lien quan

## 6. Quy tac review va merge

- Khong tu merge PR cua chinh minh neu chua co review
- Moi PR nen nho, de doc (duoi 300 dong thay doi neu co the)
- Neu co conflict, nguoi tao PR tu resolve conflict
- Merge bang `Squash and merge` de lich su commit gon

## 7. Lenh nhanh thuong dung

```bash
git status
git branch
git switch <branch>
git pull --rebase origin main
git log --oneline --graph --decorate -n 20
```

## 8. Xu ly loi thuong gap

- Push bi reject: chay `git pull --rebase origin <branch>` roi push lai
- Commit nham file: dung `git restore --staged <file>` truoc khi commit
- Sai message commit: `git commit --amend -m "fix(module): ..."` (chi dung khi chua push)

## 9. Checklist truoc khi tao PR

- Da pull code moi nhat tu `main`
- Da chay va test chuc nang co lien quan
- Commit dung quy tac
- Khong con file tam, file build khong can thiet
- Mota PR ro rang, co buoc test

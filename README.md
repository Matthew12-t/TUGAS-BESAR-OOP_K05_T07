# TUGAS-BESAR-OOP_K05

❗❔**Deskripsi Proyek** ❔❗

Spakbor Hills adalah Stardew Valley yang disederhanakan. Game ini dibangun dengan menggunakan bahasa Java. Pemain bisa berjalan-jalan menyusuri dunia piksel, bertani, berdagang, dan bahkan menikah dengan NPC.

⚙ **Fitur** ⚙
- World Map: Area luar desa (NPC house, Forest River, Mountain Lake, Ocean, Store) dapat dikunjungi dari farm.
- Gold: Mata uang, diperoleh dari menjual item di shipping bin tiap hari.
- Inventory: Tempat simpan item tanpa batas, mulai dengan seeds dan alat pertanian/fishing.
- Shipping Bin: Menjual maksimal 16 jenis item per hari, uang masuk keesokan hari.
- Action: 19 jenis aksi seperti menanam, memanen, memasak, memancing, chatting, gifting, menikah, dll.
- Fishing: Bisa di farm pond dan world map, menggunakan sistem tebak angka dengan tipe ikan berbeda.
- Cooking: Memasak butuh bahan bakar dan resep, berlangsung 1 jam dan menghabiskan energi.
- Time & Season: Waktu game real-time, musim 10 hari, cuaca berpengaruh pada tanaman dan ikan.
- End Game: Tidak ada batas waktu, milestone gold 17.209g atau menikah, setelah itu muncul statistik akhir.
- Menu: New Game, Load, Help, Player Info, Statistics, Actions, Credits, Exit.

🛠 **Dependensi** 🛠
- JDK 23
- Git
- (Opsional) IDE: IntelliJ, VSCode

🖥 **Cara Menggunakan** 🖥
1. Clone repository Github
2. Masukkan perintah berikut: javac -cp . -d . SRC\MAIN\*.java SRC\ENTITY\*.java SRC\MAP\*.java SRC\MAP\NPC_HOUSE\*.java SRC\UI\*.java SRC\INVENTORY\*.java SRC\ITEMS\*.java SRC\SEASON\*.java SRC\WEATHER\*.java SRC\TIME\*.java SRC\DATA\*.java SRC\CHEAT\*.java SRC\ENDGAME\*.java SRC\MAIN\MENU\*.java SRC\SHIPPINGBIN\*.java
3. Tunggu sampai kompilasi selesai lalu masukkan perintah berikut untuk bermain: javac -cp . SRC/MAIN/Main.java; java -cp . SRC.MAIN.Main
4. Selamat menikmati ✨

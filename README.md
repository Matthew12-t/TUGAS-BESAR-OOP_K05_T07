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

🖥 **Cara Menggunakan (Recommended - Makefile)** 🖥
1. Clone repository Github
2. Pastikan mingw32-make terinstall (atau gunakan build.bat untuk Windows)
3. Kompilasi dan jalankan game:
   ```
   mingw32-make run
   ```
4. Selamat menikmati ✨

## 🔧 Troubleshooting

**Jika gambar/suara tidak muncul saat menggunakan kompilasi manual:**
```bash
# Pastikan RES directory ada di classpath:
java -cp "build/classes;RES;." SRC.MAIN.Main
```


🖥 **Cara Manual (Alternatif)** 🖥
1. Clone repository Github
2. Kompilasi semua file Java (termasuk subdirektori):
   ```
   javac -cp . -d build/classes SRC/MAIN/*.java SRC/ENTITY/*.java SRC/ENTITY/ACTION/*.java SRC/MAP/*.java SRC/MAP/NPC_HOUSE/*.java SRC/UI/*.java SRC/INVENTORY/*.java SRC/ITEMS/*.java SRC/SEASON/*.java SRC/WEATHER/*.java SRC/TIME/*.java SRC/DATA/*.java SRC/CHEAT/*.java SRC/ENDGAME/*.java SRC/MAIN/MENU/*.java SRC/SHIPPINGBIN/*.java SRC/STORE/*.java SRC/COOKING/*.java SRC/TILES/*.java SRC/OBJECT/*.java SRC/UTIL/*.java
   ```
3. Jalankan game:
   ```
   java -cp build/classes SRC.MAIN.Main
   ```

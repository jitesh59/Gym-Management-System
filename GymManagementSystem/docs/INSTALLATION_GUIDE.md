# Installation Guide — Gym Management System

This guide walks you through setting up and running the Gym Management
System on Windows, macOS, or Linux, using NetBeans, Eclipse, IntelliJ IDEA,
or the plain command line.

---

## 1. Prerequisites

- **JDK 17 or above** installed.
  - Check with: `java -version` and `javac -version`
  - Download from https://adoptium.net if needed.
- One of the following IDEs (optional, but recommended for a final-year
  project submission):
  - NetBeans 17+
  - Eclipse IDE for Java Developers
  - IntelliJ IDEA (Community or Ultimate)

No other software, drivers, or database engines are required — the
application is 100% self-contained.

---

## 2. Getting the Project onto Your Machine

1. Copy/extract the `GymManagementSystem` folder anywhere on your disk,
   e.g. `C:\Projects\GymManagementSystem` or `~/Projects/GymManagementSystem`.
2. Confirm the folder contains: `src/`, `tools/`, `docs/`, `README.md`.

---

## 3. Running in NetBeans

1. **File → New Project → Java with Existing Sources**.
2. When prompted for the source folder, choose the `src` folder inside
   `GymManagementSystem`.
3. Right-click the project → **Properties → Sources** and make sure
   `src` is listed as a source root.
4. Right-click `Main.java` (in the default package) → **Run File**.
5. NetBeans will compile everything and launch the Login window.

---

## 4. Running in Eclipse

1. **File → New → Java Project**. Name it `GymManagementSystem`. Uncheck
   "Use default location" if you want to point directly at the extracted
   folder, or create the project then copy the `src` contents in.
2. Right-click the project → **Properties → Java Build Path → Source**,
   ensure the `src` folder is added.
3. Right-click `Main.java` → **Run As → Java Application**.

---

## 5. Running in IntelliJ IDEA

1. **File → Open** and select the `GymManagementSystem` folder.
2. IntelliJ may ask to configure a Project SDK — pick your installed JDK
   17+.
3. Mark `src` as **Sources Root** (right-click `src` → *Mark Directory
   as → Sources Root*) if it isn't automatically detected.
4. Open `Main.java`, click the green ▶ run icon next to `public class Main`.

---

## 6. Running from the Command Line (any OS)

```bash
cd GymManagementSystem

# Compile all sources into a bin/ folder
mkdir -p bin
javac -d bin -cp src $(find src -name "*.java")     # Linux/macOS
#   Windows (PowerShell):
#   Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } > sources.txt
#   javac -d bin -cp src "@sources.txt"

# Run the application
java -cp bin Main
```

---

## 7. Loading Sample Data (Recommended for First Run)

The application works fine with an empty `data/` folder — it will create
the folder and a default `admin` account automatically. If you'd like to
start with realistic sample records (8 members, 4 trainers, equipment,
attendance, and payments) instead of an empty system, run the included
generator once, **before or after** your first run:

```bash
javac -d bin -cp bin tools/SampleDataGenerator.java
java -cp bin SampleDataGenerator
```

This writes `.dat` files into the `data/` folder. You can delete the
`data/` folder at any time to reset the system to a clean slate — it will
be recreated automatically the next time you log in.

---

## 8. Default Login Credentials

```
Username: admin
Password: admin123
```

Security question (for Forgot Password): *"What is your favorite color?"*
Answer: `blue`

You can change the password any time via the **Forgot Password?** link on
the login screen, or by editing `AuthController`'s seed values before
first run.

---

## 9. Troubleshooting

| Problem | Solution |
|---|---|
| `javac` not recognized | Install a JDK (not just a JRE) and ensure its `bin/` folder is on your `PATH`. |
| Blank/garbled fonts on Linux | Install a font package such as `fonts-dejavu` — Swing relies on system fonts. |
| "package model does not exist" errors | Make sure you compiled with `-cp src` (or that `src` is marked as the Sources Root in your IDE) so package folders resolve correctly. |
| Data doesn't persist between runs | Confirm you're always running `java` from the same working directory — the `data/` folder is created relative to the current directory. |
| Window opens too small/large | Resize normally; the main window has a minimum size but is otherwise resizable. |

---

## 10. Uninstalling / Resetting

Simply delete the `GymManagementSystem` folder. All application data
lives inside `data/`, so there is no external install footprint (no
registry entries, no system services).

# Gym Management System

A complete, standalone **desktop application** for managing a gym's members,
trainers, memberships, attendance, payments, and equipment — built entirely
with **Core Java and Java Swing**. No database, no web technologies, no
JavaFX. All data is persisted locally using Java **object serialization**
(`.dat` files) inside the `data/` folder.

---

## ✨ Features

| Module | Capabilities |
|---|---|
| **Login** | Admin authentication, forgot-password recovery (security question), logout, session handling |
| **Dashboard** | Live summary cards (total/active/expired members, today's attendance, monthly revenue, trainers, equipment) + quick actions |
| **Members** | Add / Edit / Delete / Search, auto-generated Member ID, BMI calculation, membership status |
| **Memberships** | Manage plans (Monthly/Quarterly/Half-Yearly/Annual), auto expiry calculation, renewals, upcoming-renewal reminders |
| **Trainers** | Add / Edit / Delete / Search, assigned-member tracking |
| **Attendance** | Mark Present/Absent, daily & monthly history, search |
| **Payments** | Fee collection, auto receipt generation (exported as `.txt`), payment history, pending payments |
| **Equipment** | Add / Edit / Delete / Search, low-stock alerts |
| **Reports** | Active/Expired members, monthly revenue, attendance, equipment, trainer reports — all exportable to `.txt` |
| **Search** | Fast HashMap-backed search across members, trainers, and equipment |

---

## 🛠 Technology Stack

- **Java 17+** (Core Java only)
- **Java Swing** for the GUI
- **Java Collections** — `ArrayList`, `HashMap`, `List`
- **File Handling** — Java Object Serialization (`ObjectInputStream` / `ObjectOutputStream`) for `.dat` files, plain text export for reports/receipts
- **MVC architecture** — `model`, `view`, `controller` packages
- **No database** (no MySQL/Oracle/SQLite), **no JavaFX**

---

## 📁 Project Structure

```
GymManagementSystem/
│
├── src/
│   ├── model/          # Member, Trainer, Equipment, MembershipPlan, Attendance, Payment, User
│   ├── view/            # Swing GUI screens (LoginView, MainFrame, DashboardPanel, ...Panel)
│   ├── controller/      # Business logic (MemberController, TrainerController, ...)
│   ├── utils/           # FileManager, Validator, IDGenerator, DateUtil, UITheme
│   └── Main.java        # Application entry point
│
├── tools/
│   └── SampleDataGenerator.java   # One-time utility that seeds data/ with sample records
│
├── data/                # Generated at runtime — all .dat files + exported reports/receipts live here
│   ├── users.dat
│   ├── members.dat
│   ├── trainers.dat
│   ├── attendance.dat
│   ├── payments.dat
│   ├── equipment.dat
│   └── plans.dat
│
└── docs/                # README, Installation Guide, User Manual, Project Report
```

> **Note on file locations:** the assignment brief lists the `.dat` files at
> the project root. This build stores them in a `data/` subfolder instead
> (created automatically on first run) purely to keep the project root
> tidy — the file-handling mechanism (Java Object Streams, no database) is
> exactly as specified. You can change `FileManager.DATA_DIR` to `""` if you
> prefer the files at the root.

---

## ▶️ How to Run

### Option A — NetBeans / Eclipse / IntelliJ IDEA
1. Open the `GymManagementSystem` folder as a project (or create a new
   Java project and point its source folder at `src/`).
2. Set `Main.java` as the main class.
3. Build and Run.

### Option B — Command line
```bash
cd GymManagementSystem
mkdir -p bin
javac -d bin -cp src $(find src -name "*.java")
java -cp bin Main
```

### First-time sample data (optional but recommended)
```bash
javac -d bin -cp bin tools/SampleDataGenerator.java
java -cp bin SampleDataGenerator
```
This populates `data/` with 8 sample members, 4 trainers, 7 equipment
items, sample attendance, and sample payments, so the Dashboard and every
module has something to show immediately.

### Default login
```
Username: admin
Password: admin123
```

See **docs/INSTALLATION_GUIDE.md** for full setup instructions and
**docs/USER_MANUAL.md** for how to use each module.

---

## 🧱 OOP & Design Notes

- **Encapsulation** — all model classes use private fields with
  getters/setters.
- **Abstraction** — controllers hide file I/O and business rules behind
  simple method calls (`addMember`, `renewMembership`, `getLowStockEquipment`, ...).
- **Inheritance/Polymorphism** — Swing panels extend `JPanel`/`JFrame` and
  override lifecycle methods; `Member.getBMICategory()` demonstrates
  computed/polymorphic-style behavior based on state.
- **MVC** — `model` holds data, `view` holds Swing UI, `controller` holds
  logic; views never touch files directly, only controllers do.
- **Collections** — `ArrayList` for ordered storage, `HashMap` for O(1)
  ID-based lookup, used throughout every controller.
- **Exception Handling** — validation utility (`Validator`) plus try/catch
  around all file and parsing operations (`FileManager`, date parsing in
  panels).

---

## 📄 Deliverables Included

- Complete source code (`src/`)
- Sample data generator + generated sample `.dat` files (`tools/`, `data/`)
- `docs/INSTALLATION_GUIDE.md`
- `docs/USER_MANUAL.md`
- `docs/PROJECT_REPORT.md`
- `docs/VIVA_QUESTIONS.md`

Enjoy managing your gym! 🏋️

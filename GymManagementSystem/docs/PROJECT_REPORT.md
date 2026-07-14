# Project Report — Gym Management System

## 1. Introduction
The Gym Management System is a standalone Java desktop application that
helps a gym administrator manage members, trainers, memberships,
attendance, payments, and equipment without relying on a database. It is
built with Core Java, Java Swing, Java Collections, and file-based
persistence using object serialization.

## 2. Objective
To design and implement a professional, fully functional desktop
application demonstrating Object-Oriented Programming, MVC architecture,
Java Collections, and File Handling — suitable as a final-year or
training project.

## 3. Scope
The system covers ten core areas: authentication, dashboard analytics,
member management, membership plans, trainer management, attendance,
payments/receipts, equipment inventory, reporting, and search — all
accessible through a single Swing GUI shell with sidebar navigation.

## 4. Technology Stack
- Java 17+ (Core Java)
- Java Swing (GUI)
- Java Collections: `ArrayList`, `HashMap`
- Java Object Serialization (`ObjectInputStream`/`ObjectOutputStream`)
  for `.dat` files
- No database, no JavaFX, no external frameworks

## 5. System Architecture (MVC)
- **Model** (`model` package) — plain data classes: `Member`, `Trainer`,
  `Equipment`, `MembershipPlan`, `Attendance`, `Payment`, `User`. Each
  implements `Serializable` and encapsulates its fields behind
  getters/setters.
- **View** (`view` package) — Swing screens: `LoginView`, `MainFrame`
  (sidebar + CardLayout shell), and one panel per module
  (`DashboardPanel`, `MemberPanel`, `MembershipPanel`, `TrainerPanel`,
  `AttendancePanel`, `PaymentPanel`, `EquipmentPanel`, `ReportPanel`).
- **Controller** (`controller` package) — business logic and
  persistence: `AuthController`, `MemberController`,
  `MembershipController`, `TrainerController`, `AttendanceController`,
  `PaymentController`, `EquipmentController`, `ReportController`. Views
  never touch files directly; they always go through a controller.
- **Utils** (`utils` package) — cross-cutting helpers: `FileManager`
  (generic save/load for any serializable list, plus text report
  export), `Validator` (input validation), `IDGenerator` (sequential
  prefixed IDs), `DateUtil` (parsing/formatting/expiry calculation),
  `UITheme` (shared colors/fonts).

## 6. Object-Oriented Design Principles Applied
- **Encapsulation** — every model class exposes private fields only
  through getters/setters.
- **Abstraction** — controllers hide file I/O and business rules behind
  simple, intention-revealing methods (`addMember`, `renewMembership`,
  `getLowStockEquipment`).
- **Inheritance** — all Swing screens extend `JPanel`/`JFrame` and reuse
  Swing's component hierarchy and event model.
- **Polymorphism** — computed, state-dependent behavior such as
  `Member.getBMICategory()` and `Member.getStatus()` (which
  re-evaluates against the current date every time it's called) return
  different results depending on internal state without external code
  needing to know the details.

## 7. Data Structures Used
- `ArrayList<T>` — the canonical ordered store for every entity list
  (members, trainers, equipment, attendance, payments, plans).
- `HashMap<String, T>` — a parallel index keyed by ID in each
  controller, giving O(1) lookup for search/update/delete instead of
  scanning the full list.
- `List<String>` inside `Trainer` — tracks assigned member IDs.

## 8. File Handling Strategy
`FileManager` centralizes all persistence:
- `saveList(fileName, list)` / `loadList(fileName)` — generic
  serialization of any `List<T extends Serializable>` to/from `.dat`
  files inside `data/`.
- `writeTextReport(fileName, content)` — overwrite-style text export,
  used for reports and payment receipts.
- `appendTextLine(fileName, line)` — append-style text logging.

Every controller loads its list once at startup and re-saves it after
every mutating operation (add/update/delete/renew/payment), so data
survives application restarts without any explicit "Save" action from
the user.

## 9. Exception & Validation Handling
- `Validator` centralizes checks for empty fields, email format, 10-digit
  phone numbers, valid ages, and numeric strings, so every form (Member,
  Trainer, Equipment) shows a clear `JOptionPane` warning instead of
  crashing on bad input.
- `FileManager` wraps all stream operations in try/catch, logging errors
  and falling back to an empty list rather than propagating exceptions
  to the UI.
- Duplicate ID checks in every controller's `add...()` method prevent
  overwriting existing records.
- Date parsing (`DateUtil.parse`) throws `DateTimeParseException` on bad
  input, which every panel catches and reports to the user in plain
  language.

## 10. Key Modules — Summary of Functionality
1. **Login** — file-based authentication with a seeded default admin
   account, security-question password recovery, and session state held
   in `AuthController`.
2. **Dashboard** — real-time aggregate statistics recomputed from the
   live controllers.
3. **Members** — full CRUD, auto ID generation, automatic BMI/BMI
   category, automatic status (Active/Expired) based on expiry date.
4. **Memberships** — plan CRUD, automatic expiry-date calculation,
   renewal logic that extends from the later of "today" or the existing
   expiry date, and a 7-day renewal reminder list.
5. **Trainers** — full CRUD plus assigned-member tracking.
6. **Attendance** — Present/Absent marking with duplicate-day
   protection, member/date/monthly queries.
7. **Payments** — fee collection with GST/discount handling, automatic
   receipt numbering, formatted receipt text exported to file, pending
   vs. paid filtering, and monthly revenue aggregation.
8. **Equipment** — full CRUD plus a low-stock threshold alert.
9. **Reports** — six report types generated from live controller data
   and exportable as `.txt` files.
10. **Search** — HashMap-backed fast lookup by ID, name, or phone across
    members, trainers, and equipment.

## 11. Testing
The project was compiled cleanly with `javac` (JDK 21) with zero errors
or warnings across all 20 source files. A dedicated smoke test
(exercising login, CRUD, search, low-stock detection, attendance
marking, payment collection/receipt generation, membership renewal, and
report generation against realistic sample data) ran end-to-end without
exceptions, confirming the controller layer's correctness independent of
the GUI.

## 12. Sample Data
`tools/SampleDataGenerator.java` seeds the system with 8 members (a mix
of active and expired), 4 trainers, 7 equipment items (including 3 at
low stock), 7 attendance records, and 5 payments (including one
pending), so every screen has meaningful data to demonstrate immediately
after setup.

## 13. Limitations & Future Enhancements
- No encryption of stored `.dat` files — acceptable for a
  single-admin, offline training project, but a production version
  should encrypt sensitive fields.
- No multi-admin/role-based access — only a single admin role is
  modeled.
- Photo upload for members is supported at the data-model level
  (`photoPath`) but no file-chooser UI is wired up in this build.
- A `Stack`-based Undo feature was scoped as optional in the
  requirements and is not implemented in this build; the architecture
  (single point of mutation per controller) makes it straightforward to
  add later.

## 14. Conclusion
The Gym Management System successfully demonstrates a complete,
professional-grade Java Swing desktop application built on solid MVC and
OOP foundations, using only Core Java features — Collections and file
serialization in place of a database — while remaining fully functional,
validated, and ready to run in any standard Java IDE.

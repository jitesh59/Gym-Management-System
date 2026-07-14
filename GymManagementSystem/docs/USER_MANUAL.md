# User Manual — Gym Management System

A step-by-step guide to using every screen of the application.

---

## 1. Logging In

1. Launch the application (see Installation Guide).
2. Enter your **Username** and **Password** (default: `admin` / `admin123`).
3. Click **LOGIN**.
4. Forgot your password? Click **Forgot Password?**, enter your username,
   answer the security question, and set a new password.
5. To leave the system, use **Logout** at the bottom of the sidebar on any
   screen — you'll be returned to the Login screen.

---

## 2. Dashboard

The Dashboard is the first screen after login. It shows eight cards:

- **Total Members** — everyone ever registered.
- **Active Members** — members whose expiry date hasn't passed.
- **Expired Memberships** — members whose expiry date has passed.
- **Today's Attendance** — check-ins recorded for today's date.
- **Monthly Revenue** — total of all "Paid" payments this calendar month.
- **Trainers** — total trainers on staff.
- **Equipment** — total equipment items tracked.
- **Quick Actions** — a "Refresh Stats" button to recompute all cards
  after making changes elsewhere.

Use the **left sidebar** to navigate to any module at any time.

---

## 3. Member Management

**Add a member:**
1. Go to **Members**.
2. Fill in the form on the right (Name, Age, Phone, Email, etc. are
   required — marked with `*`).
3. Choose a **Membership Plan** — the **Expiry Date** field fills in
   automatically based on the Join Date and plan duration.
4. Click **Add**. A new Member ID (e.g. `MEM009`) is generated automatically.

**Edit a member:**
1. Click any row in the table — the form fills in with that member's data.
2. Change any field.
3. Click **Update**.

**Delete a member:**
1. Select the row, click **Delete**, confirm.

**Search:**
- Type a name, Member ID, or phone number into the search box and click
  **Search**. Click **Clear** to show everyone again.

**BMI:** shown automatically as a column in the table (height/weight are
optional — BMI shows as `0` if either is left blank).

---

## 4. Membership Management

Go to **Memberships** to:

- **View/Add/Edit/Delete Plans** (left panel) — every plan has a name,
  duration in months, and price.
- **Renew a Membership** (top right) — enter a Member ID and choose a
  new plan, then click **Renew**. The new expiry date is calculated from
  today (or from the current expiry date if it's still in the future).
- **Renewal Reminders** (bottom right) — automatically lists members
  whose membership expires within the next 7 days, so you know who to
  follow up with.

---

## 5. Trainer Management

Go to **Trainers** to Add / Edit / Delete / Search trainers, the same way
as Members. Each trainer record tracks how many members are currently
assigned to them (shown in the **Assigned** column).

---

## 6. Attendance Management

Go to **Attendance**:

- **Mark Attendance** — enter a Member ID and a date (defaults to today),
  then click **Mark Present** or **Mark Absent**. Marking the same
  member twice on the same date updates their status rather than
  creating a duplicate row.
- **Search** — find attendance history by member name or ID.
- **Monthly Report** — enter a month and year, click **View Monthly
  Report** to see every attendance record for that period.

---

## 7. Payment Management

Go to **Payments**:

1. Enter the **Member ID**, the base **Amount**, optional **GST** and
   **Discount**, choose a **Payment Mode** and **Status**.
2. Click **Collect Payment & Generate Receipt**.
3. A receipt is displayed on-screen and also saved as a `.txt` file in
   the `data/` folder (e.g. `receipt_RCP006.txt`) so it can be printed or
   emailed later.
4. Use **All Payments** / **Pending Payments** buttons above the table to
   filter the payment history.

---

## 8. Equipment Management

Go to **Equipment** to Add / Edit / Delete / Search equipment, plus:

- **Low Stock Alert** — click to instantly filter the table down to
  items at or below the low-stock threshold (2 units by default), so you
  know what to reorder.

---

## 9. Reports

Go to **Reports** and click any button on the left to generate that
report in the text viewer:

- Active Members Report
- Expired Members Report
- Equipment Report
- Trainer Report
- Monthly Revenue Report (enter month/year first)
- Monthly Attendance Report (enter month/year first)

Click **Export Current Report to File** to save whatever is currently
displayed as a `.txt` file inside `data/`.

---

## 10. Tips

- All dates in forms use **dd-MM-yyyy** format (e.g. `08-07-2026`).
- Fields marked `*` are mandatory; the system will show a clear
  validation message if something is missing or invalid (e.g. a
  10-digit phone number, or a properly formatted email).
- Every module's ID (Member, Trainer, Equipment, Attendance, Receipt) is
  generated automatically — you never need to invent one yourself.
- Data is saved to disk immediately after every Add/Update/Delete/Payment
  action, so there's no separate "Save" button to remember.

package controller;

import model.Member;
import utils.FileManager;
import utils.IDGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MemberController.java
 * Handles all business logic for Member Management:
 * Add, Edit, Delete, Search, and persistence.
 *
 * Uses a HashMap (memberIndex) keyed by memberId for fast lookup,
 * alongside the master ArrayList (members) which preserves insertion order.
 */
public class MemberController {

    private static final String MEMBERS_FILE = "members.dat";

    private List<Member> members;
    private Map<String, Member> memberIndex; // fast search by ID

    public MemberController() {
        members = FileManager.loadList(MEMBERS_FILE);
        rebuildIndex();
    }

    private void rebuildIndex() {
        memberIndex = new HashMap<>();
        for (Member m : members) {
            memberIndex.put(m.getMemberId(), m);
        }
    }

    public String getNextMemberId() {
        return IDGenerator.generateMemberId(members.size());
    }

    public boolean addMember(Member member) {
        if (memberIndex.containsKey(member.getMemberId())) {
            return false; // duplicate ID
        }
        members.add(member);
        memberIndex.put(member.getMemberId(), member);
        save();
        return true;
    }

    public boolean updateMember(Member updated) {
        Member existing = memberIndex.get(updated.getMemberId());
        if (existing == null) return false;
        int idx = members.indexOf(existing);
        members.set(idx, updated);
        memberIndex.put(updated.getMemberId(), updated);
        save();
        return true;
    }

    public boolean deleteMember(String memberId) {
        Member existing = memberIndex.remove(memberId);
        if (existing == null) return false;
        members.remove(existing);
        save();
        return true;
    }

    public Member getMemberById(String memberId) {
        return memberIndex.get(memberId);
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Searches members by name, ID, or phone number (case-insensitive, partial match).
     */
    public List<Member> search(String query) {
        List<Member> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return getAllMembers();
        }
        String q = query.trim().toLowerCase();
        for (Member m : members) {
            if (m.getMemberId().toLowerCase().contains(q)
                    || m.getFullName().toLowerCase().contains(q)
                    || (m.getPhoneNumber() != null && m.getPhoneNumber().contains(q))) {
                results.add(m);
            }
        }
        return results;
    }

    public int getActiveMemberCount() {
        int count = 0;
        for (Member m : members) {
            if ("Active".equals(m.getStatus())) count++;
        }
        return count;
    }

    public int getExpiredMemberCount() {
        int count = 0;
        for (Member m : members) {
            if ("Expired".equals(m.getStatus())) count++;
        }
        return count;
    }

    public int getTotalMemberCount() {
        return members.size();
    }

    private void save() {
        FileManager.saveList(MEMBERS_FILE, members);
    }
}

package com.arun.model;

import java.util.Date;

public class Record {
    int recordId;
    int studentId;
    int teacherId;
    Date recordDate;
    boolean isPresent = false;

    public Record(int studentId, int teacherId, Date date, boolean isPresent, int recordId) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.recordDate = date;
        this.isPresent = isPresent;
        this.recordId = recordId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}

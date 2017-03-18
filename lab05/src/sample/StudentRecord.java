package sample;

/**
 * Created by christien on 11/03/17.
 */
public class StudentRecord {


    private String StudentID;
    private float Assignments;
    private float Midterm;
    private float FinalExam;
    private float FinalMark;
    private char LetterGrade;

    StudentRecord(String StudentID, float Midterm, float Assignments, float FinalExam){

        this.StudentID = StudentID;
        this.Midterm = Midterm;
        this.Assignments = Assignments;
        this.FinalExam = FinalExam;

        FinalMark = (Midterm * 0.3f) + (Assignments * 0.2f) + (FinalExam * 0.5f);


    }

    public char getLetterGrade() {
        if (FinalMark >= 80 && FinalMark <= 100)
            LetterGrade = 'A';
        else if (FinalMark >= 70 && FinalMark < 80)
            LetterGrade= 'B';
        else if (FinalMark >= 60 && FinalMark < 70)
            LetterGrade = 'C';
        else if (FinalMark >= 50 && FinalMark< 60)
            LetterGrade = 'D';
        else
            LetterGrade = 'F';

        return LetterGrade;
    }

    public String getStudentID() {
        return StudentID;
    }

    public float getMidterm() {
        return Midterm;
    }

    public float getAssignments() {
        return Assignments;
    }

    public float getFinalExam() {
        return FinalExam;
    }

    public  float getFinalMark() { return FinalMark; }



}

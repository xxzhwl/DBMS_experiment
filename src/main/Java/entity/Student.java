package entity;

public class Student {
    private String SID;
    private String NAME;


    public String get(String variablename){
        if(variablename.equals("SID")){
            return SID;
        }else if(variablename.equals("NAME")){
            return NAME;
        }else{

        }
        return null;
    }
    public void set(String variablename,String value){
        if(variablename.equals("SID")){
            this.SID = value;
        }else if(variablename.equals("NAME")){
            this.NAME = value;
        }else{

        }
    }

    public String getSid() {
        return SID;
    }

    public void setSid(String sid) {
        this.SID = sid;
    }

    public String getName() {
        return NAME;
    }

    public void setName(String name) {
        this.NAME = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + SID +
                ", name='" + NAME + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!SID.equals(student.SID)) return false;
        return NAME.equals(student.NAME);
    }

    @Override
    public int hashCode() {
        int result = SID.hashCode();
        result = 31 * result + NAME.hashCode();
        return result;
    }
}

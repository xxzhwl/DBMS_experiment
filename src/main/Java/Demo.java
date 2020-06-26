import entity.Student;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.HashSet;
import java.util.Scanner;

public class Demo {

    public static HashSet<Student> studentList = new HashSet<Student>();
    public static HashSet<Student> newstudentList = new HashSet<Student>();
    public static HashSet<Student> studentListtemp = new HashSet<Student>();
    public static Scanner sc;

    public static void init(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据库初始化完毕，目前数据库条目为0");
        sc = new Scanner(System.in);
        System.out.print("请输入插入数据条目数:");
        int count = sc.nextInt();
        Student student;
        String id ;
        String name;
        sc = new Scanner(System.in);
        for(int i = 0 ;i<count;i++){
            student = new Student();
            System.out.print("请输入此学生的学号:");
            id = sc.nextLine();
            while(id==null||id.equalsIgnoreCase("")){
                System.out.print("请重新输入（不允许为空）:");
                id = sc.nextLine();
            }
            student.setSid(id.toUpperCase());
            System.out.print("请输入此学生的姓名(请使用英文字母):");
            name = sc.nextLine();
            while(name==null||name.equalsIgnoreCase("")){
                System.out.print("请重新输入（不允许为空）:");
                name = sc.nextLine();
            }
            student.setName(name.toUpperCase());
            studentList.add(student);
            try {
                System.out.println("***********等待插入数据***********");
                Thread.sleep(1000);
                System.out.println("***********插入数据成功***********");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseSql(){
        System.out.println("请输入SQL语句(务必按照以下例子输入):");
        System.out.println("查询单个例如：SELECT * FROM STUDENT WHERE SID = *** AND NAME = ***");
        System.out.println("插入单个例如：INSERT INTO STUDENT(SID,NAME)VALUE(***,***)");
        System.out.println("删除单个例如：DELETE FROM STUDENT WHERE SID = *** AND NAME = ***");
        System.out.println("更新单个例如：UPDATE STUDENT SET SID = *** , NAME = *** WHERE SID = *** AND NAME = ***");
        sc = new Scanner(System.in);
        String sql = sc.nextLine();
        System.out.println(sql.toUpperCase());
        CharStream input = CharStreams.fromString(sql.toUpperCase());
        MySqlLexer lexer = new MySqlLexer(input);//新建一个词法分析器，处理输入的CharStream
        CommonTokenStream tokens = new CommonTokenStream(lexer);//新建一个词法符号的缓冲区，用于存储词法分析器将生成的词法符号
        MySqlParser parser = new MySqlParser(tokens);//新建一个语法分析器，处理词法符号缓冲区的内容
        ParseTree tree = parser.root();
        ParseTreeWalker walker1 = new ParseTreeWalker();//新建一个语法分析树的遍历器,遍历变量
        Mysqlread loader = new Mysqlread(parser);//新建一个监听器，将其传递给遍历器
        walker1.walk(loader, tree);        // 遍历语法分析树
        System.out.println(studentList);
        studentListtemp.addAll(studentList);
        if (Mysqlread.functionflag.equalsIgnoreCase("insert")) {
            Student student = new Student();
            for (int i = 0, k = Mysqlread.left.size(); i < k; i++){

                student.set(Mysqlread.left.get(i),Mysqlread.right.get(i));
            }
            studentList.add(student);
            System.out.println("插入完成以后："+studentList);
        } else if (Mysqlread.functionflag.equalsIgnoreCase("delete")) {
            for (int i = 0, k = Mysqlread.left.size(); i < k; i++) {
                for (Student student : studentListtemp) {
                    //System.out.println(student.get(loader.left.get(i)));
                    if (student.get(Mysqlread.left.get(i)).equals(Mysqlread.right.get(i))) {
                        //System.out.println(student);
                        newstudentList.add(student);
                    }
                }
                studentListtemp.clear();
                studentListtemp.addAll(newstudentList);
                newstudentList.clear();
            }

            for (Student student : studentListtemp) {
                studentList.remove(student);
            }
            System.out.println("删除完成以后："+studentList);
        } else if(Mysqlread.functionflag.equalsIgnoreCase("update")){
            for (int i = 0, k = Mysqlread.left.size(); i < k; i++) {
                for (Student student : studentListtemp) {
                    //System.out.println(student.get(loader.left.get(i)));
                    if (student.get(Mysqlread.left.get(i)).equals(Mysqlread.right.get(i))) {
                        //System.out.println(student);
                        newstudentList.add(student);
                    }
                }
                studentListtemp.clear();
                studentListtemp.addAll(newstudentList);
                newstudentList.clear();
            }
            for (Student student : studentListtemp) {
                studentList.remove(student);
                for(int i=0;i<Mysqlread.updateLeft.size();i++){
                    student.set(Mysqlread.updateLeft.get(i),Mysqlread.updateRight.get(i));
                }
                studentList.add(student);
            }
            System.out.println("更新完成以后："+studentList);
        } else {
            for (int i = 0, k = Mysqlread.left.size(); i < k; i++) {
                for (Student student : studentListtemp) {
//                    System.out.println(student.get(loader.left.get(i)));
                    if (student.get(Mysqlread.left.get(i)).equals(Mysqlread.right.get(i))) {
                        //System.out.println(student);
                        newstudentList.add(student);
                    }
                }
                studentListtemp.clear();
                studentListtemp.addAll(newstudentList);
                newstudentList.clear();
            }
            System.out.println("查询结果："+studentListtemp);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("1.请等待初始化数据库：");
        init();
        sc = new Scanner(System.in);
        boolean flag = true;
        while(flag){
            parseSql();
            System.out.println("是否继续（Y/N）？");
            if(sc.nextLine().equalsIgnoreCase("y")||sc.nextLine().equalsIgnoreCase("yes")){
                continue;
            }else {
                flag = false;
            }
        }
    }
}




import java.util.ArrayList;
import java.util.List;

public class Mysqlread extends MySqlParserBaseListener{
    public static String functionflag ;
    public static String mytablename;
    public static List<String> left;
    public static List<String> right;
    public static List<String> updateLeft;
    public static List<String> updateRight;
    MySqlParser parser;
    public Mysqlread(MySqlParser parser){
        this.parser=parser;
        functionflag = "";
        left = new ArrayList<String>();
        right = new ArrayList<String>();
        updateLeft = new ArrayList<String>();
        updateRight = new ArrayList<String>();
    }
    public void enterInsertStatement(MySqlParser.InsertStatementContext ctx){
        functionflag = "insert";
        System.out.println(functionflag);
        mytablename = ctx.getChild(2).getText();
    }
    public void enterFromClause(MySqlParser.FromClauseContext ctx){
        //这里获取了表名
       // System.out.println(ctx.getChild(1).getText());
        mytablename = ctx.getChild(1).getText();
        //然后获取约束
        //这里获取一下约束的个数，并且获得每一个约束的之间的连接词
        int logiccount=(ctx.getChild(3).getChildCount())/2+1;
        for(int i=0;i<logiccount;i++){
            int logicindex = i*2;
            String logic = ctx.getChild(3).getChild(logicindex).getText();
            //System.out.println(logic);
            String logic1 = logic.split("=")[0];
            String logic2 = logic.split("=")[1];
            //System.out.println(logic1);
            //System.out.println(logic2);
            left.add(logic1);
            right.add(logic2);
        }
    }
    public void enterUidList(MySqlParser.UidListContext ctx){
        int variablenum=(ctx.getChildCount()/2)+1;
        for(int i=0;i<variablenum;i++){
            left.add(ctx.getChild(i*2).getText());
            //System.out.println(ctx.getChild(i*2).getText());
        }
    }
    public void enterInsertStatementValue(MySqlParser.InsertStatementValueContext ctx){
        //System.out.println(ctx.getChild(2).getText());
        int valuenum = (ctx.getChild(2).getChildCount()/2)+1;
        for(int i=0;i<valuenum;i++){
            right.add(ctx.getChild(2).getChild(i*2).getText());
            //System.out.println(ctx.getChild(2).getChild(i*2).getText());
        }
    }
    public void enterSingleDeleteStatement(MySqlParser.SingleDeleteStatementContext ctx){
        //删除
        functionflag = "delete";
        mytablename = ctx.getChild(2).getText();
        int logicnum = ctx.getChild(4).getChildCount()/2+1;
        for(int i=0;i<logicnum;i++){
            //System.out.println(ctx.getChild(4).getChild(i*2).getText());
            //System.out.println(ctx.getChild(4).getChild(i*2).getChild(0).getChild(0).getText());
            //System.out.println(ctx.getChild(4).getChild(i*2).getChild(0).getChild(2).getText());
            left.add(ctx.getChild(4).getChild(i*2).getChild(0).getChild(0).getText());
            right.add(ctx.getChild(4).getChild(i*2).getChild(0).getChild(2).getText());
        }
    }
    public void enterSingleUpdateStatement(MySqlParser.SingleUpdateStatementContext ctx){
        functionflag = "update";
        mytablename = ctx.getChild(1).getText();
        int updateNodeNum = (ctx.getChildCount()-5)/2+1;
        for(int i=0;i<updateNodeNum;i++){
            updateLeft.add(ctx.getChild(i*2+3).getChild(0).getText());
            updateRight.add(ctx.getChild(i*2+3).getChild(2).getText());
            //System.out.println(ctx.getChild(i*2+3).getChild(0).getText());
            //System.out.println(ctx.getChild(i*2+3).getChild(2).getText());
        }
        int logicNum = (ctx.getChild(ctx.getChildCount()-1).getChildCount()/2)+1;
        for(int j=0;j<logicNum;j++){
            left.add(ctx.getChild(ctx.getChildCount()-1).getChild(j*2).getChild(0).getChild(0).getText());
            right.add(ctx.getChild(ctx.getChildCount()-1).getChild(j*2).getChild(0).getChild(2).getText());
            //System.out.println(ctx.getChild(ctx.getChildCount()-1).getChild(j*2).getChild(0).getChild(0).getText());
            //System.out.println(ctx.getChild(ctx.getChildCount()-1).getChild(j*2).getChild(0).getChild(2).getText());
        }

    }
}

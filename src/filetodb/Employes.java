package filetodb;
import java.io.File;
 
import java.sql.*;
import java.util.Scanner;

public class Employes {
    private String empName;
    private int empSalary;
    private int empDeptId;
 

    public void readData(){
        try(Scanner input = new Scanner(new File("src/emp_data.txt"))) {
            //line based processing
           while(input.hasNextLine()){
               empName = "";
               String line;

               line = input.nextLine();
            
               // check if there is a empty line the text file and continue with the filled line
               if(line.length() <= 0){
                continue;
               }
               //now process the line of text for each data item
               // tokenbased processing
               try(Scanner data = new Scanner(line)) {
                   // loop until integer is detected - see data build
                   while(!data.hasNextInt()){
                       empName += data.next()+ " ";

                   }
                   // remove white space in the name
                   empName = empName.trim();

                   //get salary
                   if(data.hasNextInt()){
                       empSalary = data.nextInt();
                   }

                   // get id
                   if(data.hasNextInt()){
                       empDeptId = data.nextInt();
                   }
                   
               } 
               //System.out.println(empDeptId+"\t"+empName+"\t"+empSalary);
               saveData();
           } 
        } catch (Exception e) {
            System.out.println(e); 
        }
    }
    private void saveData(){
        try(Connection conn = connect();
            // use preparestatements to prevent sql injection attacks ? are place holders for value passing
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO employes VALUES(? , ? , ?) ")){

                pstat.setString(1, empName);
                pstat.setInt(2, empSalary);
                pstat.setInt(3, empDeptId);

                pstat.executeUpdate();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    public void displayData(){
        try(Connection conn = connect();
            // select all data from the table and check if it has done so
                Statement stat = conn.createStatement()) {
            boolean hasResultSet = stat.execute("SELECT * FROM employes");
            if(hasResultSet){
                // create a result set
                ResultSet result = stat.getResultSet();
                // retriebe the metaData of the table
                ResultSetMetaData metaData = result.getMetaData();

                // get number of columns
                int columncount = metaData.getColumnCount();
                // return the labels fo the columns
                for(int i = 1; i <=columncount; i++){
                    System.out.println(metaData.getColumnLabel(i)+"\t\t");
                }
                System.out.println();
                // return the rows
                while(result.next()){
                    // reordering 
                    String cachName = result.getString("emp_name");
                    int cachsalary = result.getInt("salary");
                    int cachid = result.getInt("dept_id");
                    System.out.println(cachName + "\t\t\t" + cachid + " " + cachsalary);
                    // order needs to be same
                    //System.out.printf("%-20s%4d%4d%n" , result.getString("emp_name"), result.getInt("salary"), result.getInt("dept_id"));
                }
            }
        } catch (Exception e) {
             System.out.println(e);
        }
    }
    // connect to sql or return sql exception if connnection fails
    private Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/employes", "root", "siemens00");
        } catch (SQLException | ClassNotFoundException e) {
           
            System.out.println(e);
            return null;
        }
    }
}
class FDemo {

    public static void main(String[] args){

        Employes list = new Employes();
        try {
            list.readData();
            list.displayData();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}

package filetodb;
public class Main {
   


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

package algdatOving2;

import java.util.Date;

public class rekusjon {
        public static double method1(double x, int n){
                if(n == 1){
                        return x;
                }
                if(n > 1){
                        return x * method1(x, n-1);  
                }
                return 0;
        }

        public static double method2(double x, int n){
                if(n == 1){
                        return x;
                }
                if(n % 2 == 1){ //n is an odd number
                        return x * method2(x*x, (n-1)/2);
                }
                if(n % 2 == 0){ //n is an even number
                        return method2(x*x, n/2);
                }
                return 0;
        }

        public static double method3(double x, int n){
                if(n == 1){
                        return x;
                }
                if(n > 1){
                        return Math.pow(x, n);
                }
                return 0;
        }

        public static void main(String[] args){
                double x = 5;
                int n = 11;
                System.out.println("X: " + x + " n: " + n + "\n\n");

                Date start1 = new Date();
                Date end1;
                int rounds = 0;
                double time;
                double result;
                do{
                        result = method1(x, n);
                        end1 = new Date();
                        ++rounds;
                } while (end1.getTime() - start1.getTime() < 1000);
                time = (double) (end1.getTime() - start1.getTime())/rounds;
                System.out.println("Total ms for method 1: " + time*1000000 + " Result: " + result);
                System.out.println("\n");

                Date start2 = new Date();
                Date end2;
                rounds = 0;
                time = 0;
                result = 0;
                do{
                        result = method2(x, n);
                        end2 = new Date();
                        ++rounds;      
                } while (end2.getTime() - start2.getTime() < 1000);
                time = (double) (end2.getTime() - start2.getTime())/rounds;
                System.out.println("Total ns for method 2: " + time*1000000 + " Result: " + result);
                System.out.println("\n");

                Date start3 = new Date();
                Date end3;
                rounds = 0;
                time = 0;
                result = 0;
                do{
                        result = method3(x, n);
                        end3 = new Date();
                        ++rounds;
                } while (end3.getTime() - start3.getTime() < 1000);
                time = (double) (end3.getTime() - start3.getTime())/rounds; 
                System.out.println("Total ns for method 3: " + time*1000000 + " Result: " + result);
        }
}

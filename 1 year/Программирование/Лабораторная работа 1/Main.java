public class Main {
    public static double dumbFunc(double x){
        return Math.pow(((Math.pow(Math.pow(x / 0.25, x), 3 - Math.cos(x))) + 4) / (Math.pow((2 / Math.tan(x)), 3)),
                Math.atan(Math.sin(x)));
    }
    public static void main(String[] args){
        float[] f = new float[10];
        float n = 19;
        for(int i = 0; i <= 9; i++) {
            f[i] = n;
            n -= 2;
        }
        double[] x = new double[15];
        for(int i = 0; i <= 14; i++){
            x[i] = Math.random() * 26 - 10;
        }
        double[][] t = new double[10][15];
        for(int i = 0; i <= 9; i++){
            for(int j = 0; j <= 14; j++) {
                if (f[i] == 9) {
                    t[i][j] = Math.tan(0.5 * Math.sin(x[j]));
                }
                else if (f[i] == 1 || f[i] == 7 || f[i] == 11 || f[i] == 15 || f[i] == 19){
                    t[i][j] = Main.dumbFunc(x[j]);
                }
                else{
                    t[i][j] = Math.tan(Math.asin(Math.exp(Math.cbrt(Math.abs(x[j]) / -2))));
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 14; j++) {
                System.out.printf(" %7.2f ", t[i][j]);
            }
            System.out.print("\n");
        }
    }
}
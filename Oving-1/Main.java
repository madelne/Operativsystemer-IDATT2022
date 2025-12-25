public class Main {
    public static void aksjer(int dager, int[] verdiEndringer){
        int differanse = 0;
        int kjopsDag = 0;
        int salgsDag = 0;
        int verdi = 0;
        int minVerdi = verdi;
        for (int i = 0; i < dager; i++) {
            verdi = verdi + verdiEndringer[i];
            if(verdi<minVerdi){
                minVerdi = verdi;
            }
        }
        for (int i = 0; i < dager; i++) {
            
        }
        System.out.println("Kjøpsdag:" + kjopsDag + "\nSalgsdag:" + salgsDag);
    }

    public static void main(String[] args) {
        int[] kurs = {-1, 3, -9, 2, 2, -1, 2, -1, -5};
        int dager = 9;
        aksjer(dager, kurs);
    }
}

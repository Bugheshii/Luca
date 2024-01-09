// INSERIRE I PROPRI DATI PERSONALI
// nome e cognome del candidato, matricola, data,       numero postazione
// ..............                ......     ../../....  ADT..
import java.util.Scanner;
import java.io.*;
// -------------- classe EncoderTester: da completare -----------------
public class EncoderTester 
{   public static void main(String[] args) throws FileNotFoundException, IOException
    {
      
    FileReader reader = new FileReader(args[0]);
    FileReader reader2 = new FileReader("coppie.txt");
    Scanner s = new Scanner (reader2);
    Scanner c = new Scanner(reader); 
    Encoder enc = new Encoder();
    c.useDelimiter(" ");
    String[] parola1 = new String[2];
    String[] parola2 = new String[2];
    String m2 = "";
    int i = 0;

    while(s.hasNext()) //Per mettere dentro encoder parole e codici
    {
        if((i%2)==0)
            parola1[i]=s.next();
        else
            parola2[i]=s.next();
        i++;
    }
    
    for(int j = 0; j<parola1.length; j++)
        enc.insert(parola1[j],parola2[j]);

    while(c.hasNext()) //confronto parole testo con parole nella lista coppie
    {
        for(int k = 0; k< enc.size(); k++)
        {
            if((c.next()).equals(parola1[k]))
                enc.invert();

            m2 += enc.toString(); 
            
        }
    }
    
    System.out.println(m2);
    reader.close();
    reader2.close();






    }
    


}
// -------------------- classe Encoder: da completare -------------------

class Encoder implements InvertibleMap
{

    public Encoder()
    {	
    	vSize = 0;
    	v = new StringPair[100];
    }
    
    public boolean isEmpty()
    {	
    	return(vSize==0);
    } 
    public int size()
    {	
    	return vSize;
    }     

    public void insert(Comparable key, Comparable value)
    {	
    	if(key==null)
    		throw new IllegalArgumentException();
    	if(vSize==v.length) 
    		v = resize();
    	v[vSize++] = new StringPair((String)key, (String)value);
    }

    public void remove(Comparable key)
    {	
    	if(key==null||!(key instanceof Comparable))
    		throw new MapItemNotFoundException();
    	v[binSearch(v, 0, v.length, ((String)key)) ]= v[vSize--];
    }

    public Comparable find(Comparable key)
    {	
    	if(binSearch(v, 0, v.length, ((String)key))==-1)  
    		throw new MapItemNotFoundException();
    	if (binSearch(v,0,v.length, ((String)key))==1)
    		return key;
    	return null;
    }

    public InvertibleMap invert()
    {	
    	InvertibleMap inversa = new Encoder();
    	for(int i=0 ; i<vSize ; i++)
    		inversa.insert(v[i].getCode(),v[i].getWord());
    	return inversa;
    	
    }

class MapItemNotFoundException extends RuntimeException{}

    public String toString()
    {
        String s = "";
        for (int i = 0; i < vSize; i++)
        {
            s = v[i].getWord() + " " + v[i].getCode();
            s += "\n";
        }
        return s; 
    }          
    
    private Comparable key;
    private Comparable value;
    private int vSize;
    private StringPair[]v;
    
    //METODI AUSILIARI

    private int binSearch(StringPair[] a, int from, int to, Comparable v)
    {	
    	String value = (String) v;
    	if(from > to)
    		return -1;  //NON TROVATO
    	int mid = (from + to) /2; //CIRCA IN MEZZO
    	StringPair middle = a[mid]; 
    	if(middle.getWord().compareTo(value)==0)
    		return mid; //TROVATO
    	if(middle.getWord().compareTo(value)<0) //CERCA A DESTRA
    		return binSearch(a, mid+1, to, v);
    	return binSearch(a, from, mid-1, v); //CERCA A SINISTRA
    }
    public StringPair[] resize()
    {	
    	StringPair[] newV = new StringPair[2*v.length];
    	System.arraycopy(v,0,newV,0,v.length);
    	return newV;
    }
    
    private class StringPair
    {   public StringPair(String word, String code)
        {   this.word = word; 
            this.code = code;
        }      
        // metodi (pubblici) di accesso
        public String getWord() 
        { return word; }
        public String getCode() 
        { return code; }
        //metodo toString sovrascritto
        public String toString() 
        {   return word + " " + code;}
        //campi di esemplare (privati)
        private String word;  //parola da cifrare
        private String code;  //codice associato alla parola
    }
}


/* -------------- Interfaccia InvertibleMap: non modificare !!---------------
  
    Questo tipo di dato astratto definisce un contenitore di coppie 
    "chiave valore", che hanno l'usuale significato.
    Si tratta di un tipo di dato astratto "mappa", con la particolarita` che 
    entrambi i campi sono di tipo Comparable, e con la proprieta` aggiuntiva
    che e` possibile creare la "mappa inversa" (si vedano piu` sotto i commenti
    al metodo invert).
*/

interface InvertibleMap   //non modificare!!
{
    boolean isEmpty(); // true: contenitore vuoto; false: contenitore non vuoto

    int size();       // restituisce il n. di elementi presenti nel contenitore

    /* 
        L'inserimento va sempre a buon fine; se la chiave non esiste, la coppia 
        key/value viene aggiunta alla mappa; se la chiave esiste gia`, il 
        valore ad essa associato viene sovrascritto con il nuovo valore; se key
        e` null viene lanciata IllegalArgumentException.
    */
    void insert(Comparable key, Comparable value);

    /* 
        La rimozione della chiave rimuove anche la corrispondente coppia.
        Lancia MapItemNotFoundException se la chiave non esiste
    */
    void remove(Comparable key);

    /* 
        La ricerca per chiave restituisce soltanto il valore ad essa associato
        nella mappa. Lancia MapItemNotFoundException se la chiave non esiste.
    */
    Comparable find(Comparable key);

    /* 
        Metodo che crea la "mappa inversa", ovvero un nuovo oggetto di tipo 
        InvertibleMap che, per ogni coppia key/value della mappa originale 
        (parametro implicito), contiene una corrispondente "coppia inversa" 
        value/key.
        Notare che l'operazione e` possibile perche` key e value sono entrambi
        oggetti di tipo Comparable: quindi e` possibile usare i valori value 
        come chiavi della mappa inversa.
        Se nella mappa originale (parametro implicito) sono presenti piu`
        coppie con lo stesso campo value, nella mappa inversa verra` inserita 
        una qualsiasi delle coppie inverse.
    */
    InvertibleMap invert();
}

class MapItemNotFoundException extends RuntimeException  {}


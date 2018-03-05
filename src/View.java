import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class View {
    Model model;
    InputStreamReader isr = null;
    BufferedReader br = null;

    public View(Model model) {
        this.model = model;
    }

    public void printInput() {
        System.out.print("Enter XML File Name : ");
        try {
            isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);
            model.setFName(0, br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.print("Enter XSL File Name : ");
            try {
                model.setFName(1, br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void printAnalyzing(int index) {
        switch (index) {
            case 0:
                System.out.println("\nAnalyzing xml file...");
                break;
            case 1:
                System.out.println("\nAnalyzing xsl file...");
                break;
        }
    }

    public void printAnalysisCompleted(int index) {
        switch (index) {
            case 0:
                System.out.println("\nXML file analysis completed !");
                break;
            case 1:
                System.out.println("\nXSL file analysis completed !");
                break;
        }
    }

    public void printHashMap(HashMap<String, String> hashMap) {
        for(int i = 0; i < hashMap.size(); i++)
            System.out.println("key : " + i + ", value : " + hashMap.get("" + i));
    }

    public void printImgHashMap(HashMap<String, String> hashMap) {
        for (String mapkey : hashMap.keySet())
            System.out.println("key : " + mapkey + ", value : " + hashMap.get(mapkey));
    }

    public void printCreateXml() {
        System.out.println("\nCreating final XML document...");
    }

    public void printCreateCompleted() {
        System.out.println("\nCreating final XML document is completed !");
    }
}

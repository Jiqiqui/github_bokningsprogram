import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Year;
class Gäster {
    String[] namn = new String[20];
    String[] kön = new String[20];
    String[] persnum = new String[20];
    int[] ålder = new int[20];
}
class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
        // keeps the textArea up to date
        textArea.update(textArea.getGraphics());
    }
}


public class Bokningsprogram_för_bussresa_med_20_personer extends JPanel{
    GridBagConstraints gbc = new GridBagConstraints();
    public Bokningsprogram_för_bussresa_med_20_personer(){
        setLayout(new GridBagLayout());

        JTextArea textArea = new JTextArea(35, 30);
        textArea.setPreferredSize(new Dimension(700,3000));
        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);


        Gäster gäster = new Gäster();
        AtomicInteger gästantal = new AtomicInteger(0);
        AtomicInteger vinst = new AtomicInteger();

        gbc.insets = new Insets(0,5,5,0);
        JLabel l1 = new JLabel("Personummer");
        JTextField tf1 = new JTextField();
        tf1.setPreferredSize(new Dimension(150,30));
        gbc.anchor = gbc.FIRST_LINE_START;
        add(l1,gbc);
        add(tf1,gbc);

        JLabel l2 = new JLabel("Namn");
        JTextField tf2 = new JTextField();
        tf2.setPreferredSize(new Dimension(150,30));
        gbc.anchor = gbc.FIRST_LINE_START;
        gbc.gridy = 1;
        add(l2,gbc);
        add(tf2,gbc);

        JLabel l3 = new JLabel("Kön");
        String[] kön = {"Man","Kvinna", "Vill ej uppge"};
        JComboBox cb1 =  new JComboBox(kön);
        gbc.anchor = gbc.FIRST_LINE_START;
        gbc.gridy = 2;
        add(l3,gbc);
        add(cb1,gbc);

        JButton b1 = new JButton("Lägg till bokning");
        gbc.gridx = 1;
        gbc.gridy = 3;
        b1.addActionListener(e -> {
                    for (int i = 0; i < 20; i++) {
                        if (tf1.getText().equalsIgnoreCase("") || tf2.getText().equalsIgnoreCase("")){
                            System.out.println("Ett eller flera fält är tomma");
                            break;
                        }
                        if(tf1.getText().length() != 12 || Integer.valueOf(tf1.getText().substring(0,4)) < 1900 || Integer.valueOf(tf1.getText().substring(0,4)) > 2022){
                            System.out.println("Ogiltigt personummer");
                            break;
                        }
                        if (tf1.getText().equalsIgnoreCase(gäster.persnum[i])){
                            System.out.println("Detta personummer används redan");
                            break;
                        }
                        if (gäster.persnum[i] == null && gäster.namn[i] == null){
                            gäster.persnum[gästantal.intValue()] = tf1.getText();
                            gäster.namn[gästantal.intValue()] = tf2.getText();
                            gäster.kön[gästantal.intValue()] = cb1.getSelectedItem().toString();
                            gäster.ålder[gästantal.intValue()] = Year.now().getValue() - Integer.valueOf(tf1.getText().substring(0,4));
                            System.out.println("Bokingsplats "+(gästantal.intValue()+1) +" reserveras för "+ gäster.namn[gästantal.intValue()]);
                            gästantal.addAndGet(1);
                            break;
                        }
                    }

                });

                add(b1,gbc);

                JButton b2 = new JButton("Ta bort bokning");
                gbc.gridx = 1;
                gbc.gridy = 4;

                b2.addActionListener(e -> {
                    for (int i = 0; i < 20; i++) {
                        if(tf1.getText().equals(gäster.persnum[i]) && tf2.getText().equalsIgnoreCase(gäster.namn[i]) && cb1.getSelectedItem().equals(gäster.kön[i])){
                            gäster.persnum[i] = "";
                            gäster.namn[i] = "";
                            gäster.kön[i] = "";
                            gästantal.addAndGet(-1) ;
                            System.out.println("Bokning "+ (i+1) +" borttagen");
                            break;
                        }
                        else if (i == 19){
                            System.out.println("Det finns ingen bokning med denna infomation. Var vänlig och dubbelchecka att all information stämmer");
                        }
                    }
                });
                add(b2,gbc);

                JButton b3 = new JButton("Skriv ut åldrar");
                b3.addActionListener(e -> {
                    for (int i = 0; i < 20; i++) {
                        if(gäster.namn[i] != null) {
                            System.out.println(gäster.namn[i] + "/gäst "+ (1+i)+"s ålder" + gäster.ålder[i]);
                        }
                    }
                });
                gbc.gridx = 1;
                gbc.gridy = 5;
                add(b3,gbc);

                JButton b4 = new JButton("Beräkna vinst");
                gbc.gridx = 1;
                gbc.gridy = 6;
                b4.addActionListener(e -> {
                    for(int i = 0; i < 20; i++) {
                        if(gäster.namn[i] == null) {
                            continue;
                        }
                        if(gäster.ålder[i] >= 18 && !gäster.namn[i].equalsIgnoreCase("")){
                            vinst.addAndGet(300);
                        }
                        else if (!gäster.namn[i].equalsIgnoreCase("")){
                            vinst.addAndGet(300);
                        }
                    }
                    System.out.println(vinst);
                });
                add(b4,gbc);

                JButton b5 = new JButton("Avsluta");
                gbc.gridx = 1;
                gbc.gridy = 7;
                b5.addActionListener(e -> System.exit(0));
                add(b5,gbc);
                System.out.println("Använd dig av GUI:n");

        gbc.gridheight = 0;
        gbc.gridy= 0;
        gbc.gridx = 3;
        add(scroll,gbc);
    }


    public static void main(String[] args){
        Bokningsprogram_för_bussresa_med_20_personer bok = new Bokningsprogram_för_bussresa_med_20_personer();
        JFrame jf = new JFrame();
        jf.setTitle("Bokningsprogram");
        jf.setSize(800,800);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        jf.add(bok);

    }
}

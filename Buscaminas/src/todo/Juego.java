package todo;
//El * importa todas las librerias para usar el JFrame, JPanel y JTextField
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.util.Random;

public class Juego {
    //atributos
    JFrame ventana; //Ventana en donde estara el juego
    JPanel panelPresentacion;
    JLabel fondoPresentacion;//Atributo para poner un fondo
    JTextField numMinas;
    JTextField numFC; //Numero de filas columnas
    JButton jugar;
    int filas,columnas,minas;

    //Atributos dentro del Juego
    JPanel panelJuego;
    JLabel fondoJuego;
    JLabel marcadorTiempo;
    JLabel marcadorBanderas;
    JLabel[][] matriz;
    Timer tiempo;
    int[][] mat;
    int[][] auxmat;
    Random aleatorio;
    int min;
    int seg;
    int contBanderas;
    int contRestante;

    //Constructor
    public Juego(){

        /*
        Propiedades al atributo ventana
         */
        ventana = new JFrame("Buscaminas"); //Titulo de la ventana
        ventana.setSize(650,500); //Tamanio de la ventana
        ventana.setLocationRelativeTo(null); //Se posicionara en el centor
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Permite detener la ejecucion en segundo plano
        ventana.setLayout(null);//Permite poner los elementos como querramos
        ventana.setResizable(false);//false: deshabilita el boton maximizar

        /*
        JPanel presentacion
         */
        //Contiene caracteristicas similares a la ventana
        panelPresentacion = new JPanel();

        //atributo.getWidth/getHeight permite modificar al tamanio asignado al del atributo principal
        panelPresentacion.setSize(ventana.getWidth(),ventana.getHeight());
        panelPresentacion.setLocation(0,0);
        panelPresentacion.setLayout(null);
        panelPresentacion.setVisible(true);
//        panelPresentacion.setBackground(Color.RED); //Propiedad para dar color al fondo

        /*
        Imagen de presentacion
         */
        fondoPresentacion = new JLabel();
        //Se agrego una carpeta imagen a la carpeta principal para tomar la direccion
        fondoPresentacion.setIcon(new ImageIcon("imagenes/fondoPresentacion.png"));
        //Ajustando el tamanio de los paneles
        fondoPresentacion.setBounds(0,0, panelPresentacion.getWidth(), panelPresentacion.getHeight());
        fondoPresentacion.setVisible(true);
        panelPresentacion.add(fondoPresentacion,0);//

        /*
        Caja numero de filas/columnas
         */
        numFC = new JTextField("Ingrese numero de filas/columnas");
        numFC.setSize(200,30); //Tamanio de la caja
        //Permite posicionar la el cuadro para ingresar el numero de FC
        numFC.setLocation(panelPresentacion.getWidth()-numFC.getWidth()-230,180);
        numFC.setVisible(true); //Visible
        panelPresentacion.add(numFC,0); //Anadir al panel de presentacion

        /*
        Caja numero de minas
         */
        numMinas = new JTextField("Ingrese numero de minas");
        numMinas.setSize(200,30);
        //Permite posicionar la el cuadro para ingresar el numero de FC
        numMinas.setLocation(panelPresentacion.getWidth()-numFC.getWidth()-230,220);
        numMinas.setVisible(true);
        panelPresentacion.add(numMinas,0);

        /*
        Boton Jugar
         */
        jugar = new JButton("Jugar");
        jugar.setSize(200,30);
        jugar.setLocation(panelPresentacion.getWidth()-numFC.getWidth()-230,260);
        jugar.setVisible(true);
//        jugar.setBackground(Color.white); //Anadir background
        panelPresentacion.add(jugar,0);

        /*
        JPanel Juego
         */
        //Contiene caracteristicas similares a la ventana
        panelJuego = new JPanel();

        //atributo.getWidth/getHeight permite modificar al tamanio asignado al del atributo principal
        panelJuego.setSize(ventana.getWidth(),ventana.getHeight());
        panelJuego.setLocation(0,0);
        panelJuego.setLayout(null);
        panelJuego.setVisible(true);

        /*
        Imagen de presentacion
         */
        fondoJuego = new JLabel();
        //Se agrego una carpeta imagen a la carpeta principal para tomar la direccion
        fondoJuego.setIcon(new ImageIcon("imagenes/fondoJuego.png"));
        //Ajustando el tamanio de los paneles
        fondoJuego.setBounds(0,0, panelJuego.getWidth(), panelJuego.getHeight());
        fondoJuego.setVisible(true);
        panelJuego.add(fondoJuego,0);//

        aleatorio = new Random(); //Acceso al random

        //Mostrando el marcador del tiempo
        min =1;
        seg =1;
        marcadorTiempo = new JLabel("Tiempo: "+min+":"+seg);
        marcadorTiempo.setSize(70,30);
        marcadorTiempo.setVisible(true);
        //Color de letra
        marcadorTiempo.setForeground(Color.black);
        panelJuego.add(marcadorTiempo,0);

        //Creando timer
        tiempo = new Timer (1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Contador de tiempo
                seg++;
                if (seg==60){
                    seg=0;
                    min++;
                }
                //Marcando el tiempo
                marcadorTiempo.setText("Tiempo: "+min+":"+seg);
            }
        });

        //Mostrando texto de banderas
        marcadorBanderas = new JLabel("Banderas: "+contBanderas);
        marcadorBanderas.setSize(70,30);
        marcadorBanderas.setVisible(true);
        //Color de letra
        marcadorBanderas.setForeground(Color.black);

        panelJuego.add(marcadorBanderas,0);

        /*
        Evento del click del boton jugar
         */

        //Llamado del evento al presionar el boton jugar
        jugar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e){
                //Inicio de la validacion de los campos
                System.out.println("Presione jugar");
                filas = Integer.parseInt(numFC.getText());
                columnas = Integer.parseInt(numFC.getText());
                minas = Integer.parseInt(numMinas.getText());

                //Cambiando paneles
                panelPresentacion.setVisible(false);
                panelJuego.setVisible(true);
                ventana.add(panelJuego);

                //Asignando tamanio a las variables y matriz
                mat = new int[filas][columnas];
                auxmat = new int[filas][columnas];
                matriz = new JLabel[filas][columnas];
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        matriz[i][j] = new JLabel(); //Ver  las minas colocadas
                    }
                }
                tiempo.start();
                contBanderas = minas;
                marcadorBanderas.setText("Banderas: "+contBanderas);

                //Agregar matriz de imagenes
                inicializarMatriz();

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        //Evento para clickear las imagenes de la matriz
                        matriz[i][j].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {

                            for (int k = 0; k < filas; k++) {
                                for (int l = 0; l < columnas; l++) {
                                    if(e.getSource()==matriz[k][l]){
                                        //Condicion para cuando se haga click izquierdo (button1)
                                        if(e.getButton()==MouseEvent.BUTTON1){
                                            System.out.println(k+" "+l);

                                            //Mostrando las casillas numericas
                                            if(mat[k][l]!=-2 && mat[k][l]!=0 && auxmat[k][l]!=-3){
                                                auxmat[k][l]=mat[k][l];
                                                matriz[k][l].setIcon(new ImageIcon("imagenes/"+auxmat[k][l]+".png"));
                                            }

                                            //Mostrando las casillas con bombas / perder el juego
                                            if(mat[k][l]==-2){
                                                for (int m = 0; m < filas; m++) {
                                                    for (int n = 0; n < columnas; n++) {
                                                        if(mat[m][n]==-2){
                                                            auxmat[m][n] = mat[m][n];
                                                            matriz[m][n].setIcon(new ImageIcon("imagenes/"+auxmat[m][n]+".png"));
                                                        }
                                                    }
                                                }

                                                //Mostrando mensaje de Game Over
                                                JOptionPane.showMessageDialog(ventana,"Game Over");

                                                //Opciones para reiniciar
                                                for (int m = 0; m < filas; m++) {
                                                    for (int n = 0; n < columnas; n++) {
                                                        matriz[m][n].setVisible(false);
                                                        seg=0;
                                                        min=0;
                                                        contBanderas= minas;
                                                    }
                                                }
                                                ventana.setSize(650,500);
                                                panelPresentacion.setSize(ventana.getWidth(),ventana.getHeight());
                                                fondoPresentacion.setSize(ventana.getWidth(),ventana.getHeight());
                                                panelJuego.setVisible(false);
                                                panelPresentacion.setVisible(true);
                                                ventana.add(panelPresentacion);
                                            }

                                            //Mostrando espacios en blanco
                                            if(mat[k][l]==0 && auxmat[k][l]!=-3){
                                                //Se llama a la funcion recursiva
                                                recursiva(k,l);
                                                numVecinos();
                                            }

                                            //Validacion para ganar
                                            contRestante =0;
                                            for (int m = 0; m <filas ; m++) {
                                                for (int n = 0; n < columnas; n++) {
                                                    if(auxmat[m][n]==-1 || auxmat[m][n]==-3){
                                                        contRestante++;
                                                    }
                                                }
                                            }
                                            if(contRestante== minas){
                                                JOptionPane.showMessageDialog(ventana,"You Win");
                                                //Reiniciar juego
                                                for (int m = 0; m < filas; m++) {
                                                    for (int n = 0; n < columnas; n++) {
                                                        matriz[m][n].setVisible(false);
                                                        seg=0;
                                                        min=0;
                                                        contBanderas= minas;
                                                        numVecinos();
                                                    }
                                                }

                                                ventana.setSize(650,500);
                                                panelPresentacion.setSize(ventana.getWidth(),ventana.getHeight());
                                                fondoPresentacion.setSize(ventana.getWidth(),ventana.getHeight());
                                                panelJuego.setVisible(false);
                                                panelPresentacion.setVisible(true);
                                                ventana.add(panelPresentacion);
                                            }

                                            //Condicion para el click derecho (button3)
                                            //La rueda del raton es el button2
                                        } else if(e.getButton()==MouseEvent.BUTTON3){
                                            //Sustitucion de imagen de casilla a bandera
                                            if(auxmat[k][l]==-1 && contBanderas >0){
                                                auxmat[k][l]=-3;
                                                contBanderas--;
                                                matriz[k][l].setIcon(new ImageIcon("imagenes/"+auxmat[k][l]+".png"));
                                                marcadorBanderas.setText("Bandera: "+ contBanderas);//Resta una bandera del contador
                                            } else if(auxmat[k][l]==-3){
                                                //Sustitucion de imagen de bandera a casilla
                                                auxmat[k][l]=-1;
                                                matriz[k][l].setIcon(new ImageIcon("imagenes/"+auxmat[k][l]+".png"));
                                                contBanderas++;
                                                marcadorBanderas.setText("Banderas: "+contBanderas);//Agrega una bandera al contador
                                            }
                                        }
                                    }
                                }
                            }

                        }});
                    }
                }
                //Final de la validacion de los campos
            }
        });

        //Permite anadir el vanal a la ventana
        ventana.add(panelPresentacion);
        ventana.setVisible(true);

    }

    public void inicializarMatriz(){

        //Asignando minas en posiciones aleatorias
        int f;
        int c;

        for (int i = 0; i<filas;i++)
            for (int j =0; j<columnas; j++){
                mat[i][j]=0;
                auxmat[i][j]=-1;
            }

        for(int i = 0; i < minas; i++) {
            do{
                f = aleatorio.nextInt(filas);
                c = aleatorio.nextInt(columnas);
            } while(mat[f][c]==-2); //-2 Es una mina
                mat[f][c]=-2;
        }

        //Numeros que rodean las minas
        for (int i = 0; i<filas;i++){
            for (int j = 0; j<columnas; j++){
                if( mat[i][j] == -2){

                    //Posiciones que rodean una mina
                    //Hacia arriba
                    //representa no salir del borde, 0 es una posicion en blanco de la matriz
                    if(i>0 && mat[i-1][j]!=-2){
                        mat[i-1][j]++;
                    }

                    //Hacia abajo
                    if(i<filas-1 && mat[i+1][j]!=-2){
                        mat[i+1][j]++;
                    }

                    //Hacia la derecha
                    if(j>0 && mat[i][j-1]!=-2){
                        mat[i][j-1]++;
                    }

                    //Hacia la izquierda
                    if(j<columnas-1 && mat[i][j+1]!=-2){
                        mat[i][j+1]++;
                    }

                    //Esquina sup izquierda
                    if(i>0 && j>0 && mat[i-1][j-1]!=-2){
                        mat[i-1][j-1]++;
                    }

                    //Esquina inf izquierda
                    if(i<filas-1 && j>0 && mat[i+1][j-1]!=-2){
                        mat[i+1][j-1]++;
                    }

                    //Esquina sup derecha
                    if(i>0 && j<columnas-1 && mat[i-1][j+1]!=-2){
                        mat[i-1][j+1]++;
                    }

                    //Esquina inf derecha
                    if(i<filas-1 && j<columnas-1 && mat[i+1][j+1]!=-2){
                        mat[i+1][j+1]++;
                    }

                }
            }
        }

        ventana.setSize(100+(columnas*25),150+(filas*25));
        panelJuego.setSize(100+(columnas*25),150+(filas*25));
        marcadorTiempo.setLocation(ventana.getWidth()-90,30);
        marcadorBanderas.setLocation(10,30);

        //Agregando imagenes a la matriz
        for (int i = 0; i<filas;i++){
            for (int j =0; j<columnas; j++){
                matriz[i][j].setSize(25,25);
                matriz[i][j].setLocation(j,j);
                matriz[i][j].setLocation(50+(j*25),75+(i*25));
                matriz[i][j].setIcon(new ImageIcon("imagenes/"+auxmat[i][j]+".png"));
                matriz[i][j].setVisible(true);
                panelJuego.add(matriz[i][j],0);
            }
            System.out.println(""); //Espacio entre cuadro
        }
    }
    //Fin inicializarmatriz()

    //Se crea una recursiva
    public void recursiva(int i, int j){
        auxmat[i][j]=mat[i][j];
        mat[i][j] =9;
        //yendo a la derecha
        if(j < columnas-1 && mat[i][j+1]==0 && auxmat[i][j+1]!=-3){
            recursiva(i,j+1);
        } else if(j < columnas-1 && mat[i][j+1]!=0 && mat[i][j+1]!=-2 && mat[i][j+1]!=9 && auxmat[i][j+1]!=-3) {
            auxmat[i][j+1] = mat[i][j+1];
        }

        //Yendo a la izquierda
        if(j>0 && mat[i][j-1]==0 && auxmat[i][j-1]!=-3){
            recursiva(i,j-1);
        }  else if(j > 0 && mat[i][j-1]!=0 && mat[i][j-1]!=-2 && mat[i][j-1]!=9 && auxmat[i][j-1]!=-3) {
            auxmat[i][j-1] = mat[i][j-1];
        }

        //Arriba
        if(i>0 && mat[i-1][j]==0 && auxmat[i-1][j]!=-3){
            recursiva(i-1,j);
        } else if(i >0 && mat[i-1][j]!=0 && mat[i-1][j]!=-2 && mat[i-1][j]!=9 && auxmat[i-1][j]!=-3) {
            auxmat[i-1][j] = mat[i-1][j];
        }

        //Abajo
        if(i< filas-1 && mat[i+1][j]==0 && auxmat[i+1][j]!=-3){
            recursiva(i+1,j);
        } else if(i< filas-1 && mat[i+1][j]!=0 && mat[i+1][j]!=-2 && mat[i+1][j]!=9 && auxmat[i+1][j]!=-3) {
            auxmat[i+1][j] = mat[i+1][j];
        }

        matriz[i][j].setIcon(new ImageIcon("imagenes/"+auxmat[i][j]+".png"));
    }

    public void numVecinos(){
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.println(auxmat[i][j]+" ");
            matriz[i][j].setIcon(new ImageIcon("imagenes/"+auxmat[i][j]+".png"));
            }
            System.out.println("");
        }
    }
}
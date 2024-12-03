package es.deusto.prog3.intermedio.tareas;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import es.deusto.prog3.intermedio.Clima;
import es.deusto.prog3.intermedio.Ecosistema;
import es.deusto.prog3.intermedio.Ecosistemas;
import es.deusto.prog3.intermedio.Organismo;
import es.deusto.prog3.intermedio.TipoOrganismo;

@SuppressWarnings("serial")
public class VentanaTabla extends JFrame{
	
	private DefaultTableModel modelo;
	private ArrayList<Ecosistema> ecosistemas;
	
	public VentanaTabla(ArrayList<Ecosistema> ecosistemas) {
		this.ecosistemas = ecosistemas;
		
		setTitle("Entrada de Datos de Ecosistemas");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(5,5));
        
        Vector<String> cabeceraComics = new Vector<String>(Arrays.asList( "AGUA", "CLIMA", "PLANTAS", "HERBIVIROS", "CARNIVOROS"));
		//TODO hacer no editable las dos primeras coulumnas
        modelo = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraComics);     
        
        
        //Se borran los datos del modelo de datos
        modelo.setRowCount(0);
		//Se añaden los comics uno a uno al modelo de datos
        this.ecosistemas.forEach(e -> modelo.addRow(
				new Object[] {e.getAgua(), e.getClima(), e.getNumOrganismosPorTipo(TipoOrganismo.PLANTA),
						e.getNumOrganismosPorTipo(TipoOrganismo.HERBIVORO),
						e.getNumOrganismosPorTipo(TipoOrganismo.CARNIVORO)} )
		);
		//Se crea la tabla de comics con el modelo de datos
        JTable tabla = new JTable(modelo);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(new TitledBorder("Ecosistemas"));
        
        
        
        //TODO Tarea renderer
        //1- Modificar la columna de cantidad de agua por un color diferente 10000-12000 RED, 12000-15000 CYAN,
        //15000-17000 BLUE, 17000-20000 GREEN
        //2- Si está seleccionada la fila se mostrará el dato bruto del agua con los colores por defecto de la tabla
        //3- Modificar la columna clima para que aparezca la imagen correspondiente (Direcotrio 'Data'), si está seleccionado mantener los valores por defecto
        //se añadirá un tooltip para indicar el clima en texto cuando el ratón se ubique sobre la celda
        //4- Modificar la altura de todas las filas a 35 pixels
        
        
        //TODO tarea eventos de ratón
        // 1- Modificar el título del borde del panel de la tabla para que cuando el ratón pase por una fila de la tabla, escriba el clima
        // correspondiente al ecosistema de esa línea
        // 2- Cuando el ratón salga de la ventana deberá escribir el título original
        
        
        
        
        add(scrollPane, BorderLayout.CENTER);
        
        //TODO tarea Hilos
        //Modifica el eschador del botón SIMULAR para que la ventana no se bloquee cuando simula el paso de los años en todos los ecosistemas de la tabla
        //Impleméntalo de tal manera que cada año haya una espera de 1 segundo
        //Implementa la funcionalidad necesaria para parar la simulación en cualquier momento con el botón PARAR.
        JButton simular = new JButton("SIMULAR");
        simular.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
        
        JButton parar = new JButton("PARAR");
        parar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				
			}
		});
        
        JPanel panelBotones = new JPanel(new GridLayout(1,2));
        panelBotones.add(simular);
        panelBotones.add(parar);
        add(panelBotones,BorderLayout.SOUTH);
        
        setVisible(true);
   
	}
	/**
	 * Función que actualiza la tabla sólo con los ecosistemas que siguen vivos
	 */
	private void modificarModelo() {
		modelo.setRowCount(0);
		for (Ecosistema eco: ecosistemas) {
			if (eco.vive()) {
				
				modelo.addRow(
							new Object[] {eco.getAgua(), eco.getClima(), eco.getNumOrganismosPorTipo(TipoOrganismo.PLANTA),
									eco.getNumOrganismosPorTipo(TipoOrganismo.HERBIVORO),
									eco.getNumOrganismosPorTipo(TipoOrganismo.CARNIVORO)} );
					
			}
													
		}
    }
	
	public static void main(String[] args) {
		
		ArrayList<Organismo> organismos = new ArrayList<>();
		Ecosistemas.cargarOrganismosCSV(organismos, "data/organismos.csv");
        // Crear ecosistemas viables
		ArrayList<Ecosistema> ecosistemas = Ecosistemas.generarEcosistemasViables(organismos, 100, 30);

		SwingUtilities.invokeLater(() -> new VentanaTabla(ecosistemas));
	}

}

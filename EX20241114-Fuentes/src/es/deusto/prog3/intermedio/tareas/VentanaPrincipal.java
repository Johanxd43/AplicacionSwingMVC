package es.deusto.prog3.intermedio.tareas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import es.deusto.prog3.intermedio.Ecosistema;
import es.deusto.prog3.intermedio.Ecosistemas;
import es.deusto.prog3.intermedio.Organismo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {
    private JTextField txtNumEcosistemas;
    private JTextField txtNumOrganismos;
    private JTextField txtRutaOrganismos;
    private ArrayList<Ecosistema> ecosistemas;

    public VentanaPrincipal() {
    	//TODO
    	/**
    	 * Modifica el código en diferentes puntos siguiendo el diseño de layouts de la imagen
    	 * utilizando layouts en swing (BorderLayout, FlowLayout, GridLayout...)
    	 */
        // Configuración de la ventana
        setTitle("Entrada de Datos de Ecosistemas");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel lblArchivo = new JLabel("Archivo:");
        txtRutaOrganismos = new JTextField();
        JButton btnBuscarArchivo = new JButton("...");
        
        add(lblArchivo);
        add(txtRutaOrganismos);
        add(btnBuscarArchivo);

        // Acción del botón para seleccionar archivo
        btnBuscarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("./data");
                int resultado = fileChooser.showOpenDialog(VentanaPrincipal.this);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivoSeleccionado = fileChooser.getSelectedFile();
                    txtRutaOrganismos.setText(archivoSeleccionado.getAbsolutePath());
                }
            }
        });
        
        add(new JLabel("Número de Ecosistemas para crear:"));
        add(new JLabel("Número de Organismos por ecosistema:"));
        
        txtNumEcosistemas = new JTextField(5);
        txtNumEcosistemas.setText("100");
        add(txtNumEcosistemas);
        txtNumOrganismos = new JTextField(5);
        txtNumOrganismos.setText("30");
        add(txtNumOrganismos);
        
        JLabel imagen = new JLabel(new ImageIcon("data/ecosystems2.png"));
        add(imagen);

        // Botón inferior para acción
        JButton btnCrear = new JButton("Crear Ecosistemas");
        add(btnCrear);

        // Acción del botón Crear Ecosistemas
        btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numEcosistemas = Integer.parseInt(txtNumEcosistemas.getText());
                    int numOrganismos = Integer.parseInt(txtNumOrganismos.getText());
                    ArrayList<Organismo> organismos = new ArrayList<>();

                    // Cargar organismos desde el archivo
                    Ecosistemas.cargarOrganismosCSV(organismos, txtRutaOrganismos.getText());

                    // Crear ecosistemas viables
                    ecosistemas = Ecosistemas.generarEcosistemasViables(organismos, numEcosistemas, numOrganismos);

                } catch (NumberFormatException ex) {
                    System.out.println("Por favor, ingresa valores numéricos válidos.");
                } catch (Exception ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
                
                VentanaTabla ventanaTabla = new VentanaTabla(ecosistemas);
                VentanaPrincipal.this.setVisible(false);
                
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}


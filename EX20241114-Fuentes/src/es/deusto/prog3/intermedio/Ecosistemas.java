package es.deusto.prog3.intermedio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Ecosistemas {

	/**
	 * Indica si un Organismo puede vivir en un Clima determinado
	 * @param organismo
	 * @param clima
	 * @return boolean
	 */
	private static boolean puedeVivir(Organismo organismo, Clima clima) {
		boolean puede = false;
		
		// Si el clima es compatible...
		if (organismo.getClimas().contains(clima)) {
			puede = true;
		}
		return puede;
	}

	/**
	 *  Genera ecosistemas diferentes con los organismos iniciales proporcionados en organismos
		Esta función crea ecosistemas viables teniendo en cuenta el clima para cada organismo (puedeVivir)
	 * @param organismos
	 * @param numEcosistemas
	 * @param numOrganismos
	 * @return ArrayList de Ecosistema
	 */
	public static ArrayList<Ecosistema> generarEcosistemasViables(ArrayList<Organismo> organismos, int numEcosistemas, int numOrganismos) {
		ArrayList<Ecosistema> ecosistemas = new ArrayList<Ecosistema>();
		
		for (int i = 0; i < numEcosistemas; i++) {
			// agua aleatoria de 10000 a 20000 m3
			double agua = 10000 + Math.random() * 10000;
			// clima aleatorio entre todos los de Clima.values()
			int alea = (int) (Math.random() * Clima.values().length);
			Clima clima = Clima.values()[alea];
			// mapa de organismos
			HashMap<TipoOrganismo, ArrayList<Organismo>> orgs = new HashMap<>();
			
			for(TipoOrganismo tipoOrg: TipoOrganismo.values()) {
				orgs.put(tipoOrg, new ArrayList<Organismo>());
			}
			
			for (int j = 0; j < numOrganismos; j++) {
				// Elegir un organismo aleatorio que sea viable en este ecosistema
				Organismo organismo;
				int aleat = 0;
				do {
					aleat = (int) (Math.random() * organismos.size());
					organismo = organismos.get(aleat);
				} while (!puedeVivir(organismo, clima));
				
				// Añadir el organismo al mapa orgs
				TipoOrganismo tipo;
				
				if (organismo instanceof Planta) {
					tipo = TipoOrganismo.PLANTA;
					organismo = new Planta((Planta) organismos.get(aleat));
				} else if (organismo instanceof Herbivoro) {
					tipo = TipoOrganismo.HERBIVORO;
					organismo = new Herbivoro((Herbivoro) organismos.get(aleat));
				} else {
					tipo = TipoOrganismo.CARNIVORO;
					organismo = new Carnivoro((Carnivoro) organismos.get(aleat));
				}
				
				if (orgs.containsKey(tipo)) {
					// Añadir el organismo a la lista orgs.get(tipo)
					orgs.get(tipo).add(organismo);
				} else {
					// Crear la lista y añadir
					orgs.put(tipo, new ArrayList<Organismo>());
					orgs.get(tipo).add(organismo);
				}
				
			}
			
			Ecosistema ecosistema = new Ecosistema(agua, clima, orgs);
			
			ecosistemas.add(ecosistema);
		}
		
		return ecosistemas;
	}

	/**
	 * 1) Carga de Datos desde CSV: método estático que lee datos de
		un archivo CSV (por ejemplo, “organismos.csv”) y crea objetos de las clases correspondientes en la
		lista de organismos.
		El fichero tiene este formato:
		Tipo;Especie;Edad;EdadMin;EdadMax;Reproduccion;Climas;Alimentacion
		por ejemplo:
		Planta;Hierba;5;2;10;2;TROPICAL,SECO,CONTINENTAL,TEMPLADO,POLAR;
		Herbivoro;Canguro;7;4;16;1;TROPICAL,SECO,CONTINENTAL,TEMPLADO;Bambú,Manzano
		Carnivoro;Lobo;5;2;10;1;SECO,CONTINENTAL,TEMPLADO,POLAR;Conejo,Ciervo
	 * @param organismos
	 * @param fileName
	 */
	public static void cargarOrganismosCSV(ArrayList<Organismo> organismos, String fileName) {
		File f = new File(fileName);
		try {
			Scanner sc = new Scanner(f);
			
			while(sc.hasNextLine()) {
				String linea = sc.nextLine();
				String[] campos = linea.split(";");
				String tipo = campos[0];
				String especie = campos[1];
				//double edad = Double.parseDouble(campos[2]);
				double edadMin = Double.parseDouble(campos[3]);
				double edadMax = Double.parseDouble(campos[4]);
				int reproduccion = Integer.parseInt(campos[5]);
				String climas = campos[6];
				String alimentacion = campos[7];
				
				Organismo nuevo;
				
				// Preparamos la lista de climas a partir del String climas...
				ArrayList<Clima> listaClimas = new ArrayList<Clima>();
				
				for (String clima : climas.split(",")) {
					listaClimas.add(Clima.valueOf(clima));
				}
				
				// Creamos el organismo en función de su tipo
				if (tipo.equals("Planta")) {
					// Planta
					nuevo = new Planta(especie, listaClimas, edadMin, edadMax, reproduccion, Double.parseDouble(alimentacion));
				} else if (tipo.equals("Herbivoro")) {
					// Herbivoro
					// Falta añadir las plantas a la alimentación de este herbívoro
					String[] plantas = alimentacion.split(",");
					ArrayList<Planta> alim = new ArrayList<Planta>();
					for (String planta : plantas) {
						for (Organismo organismo : organismos) {
							if (organismo.getEspecie().equals(planta)) {
								alim.add((Planta) organismo);
							}
						}
					}
					nuevo = new Herbivoro(especie, listaClimas, edadMin, edadMax, reproduccion, alim);
				} else {
					// Carnivoro
					// Falta añadir los animales a la alimentación de este herbívoro
					String[] animales = alimentacion.split(",");
					ArrayList<Animal> alim = new ArrayList<Animal>();
					for (String animal : animales) {
						for (Organismo organismo : organismos) {
							if (organismo.getEspecie().equals(animal)) {
								alim.add((Animal) organismo);
							}
						}
					}
					nuevo = new Carnivoro(especie, listaClimas, edadMin, edadMax, reproduccion, alim);
					
				}
				
				organismos.add(nuevo);
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

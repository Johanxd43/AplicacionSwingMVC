package es.deusto.prog3.intermedio;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ecosistema implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double agua;
	protected Clima clima;
	protected HashMap<TipoOrganismo, ArrayList<Organismo>> organismos;
	
	public Ecosistema(double agua, Clima clima, HashMap<TipoOrganismo, ArrayList<Organismo>> organismos) {
		super();
		this.agua = agua;
		this.clima = clima;
		this.organismos = organismos;
	}

	public double getAgua() {
		return agua;
	}

	public void setAgua(double agua) {
		this.agua = agua;
	}

	public Clima getClima() {
		return clima;
	}

	public void setClima(Clima clima) {
		this.clima = clima;
	}

	public HashMap<TipoOrganismo, ArrayList<Organismo>> getOrganismos() {
		return organismos;
	}

	public void setOrganismos(HashMap<TipoOrganismo, ArrayList<Organismo>> organismos) {
		this.organismos = organismos;
	}
	
	public int getNumOrganismosPorTipo(TipoOrganismo tipo) {
		if(organismos.containsKey(tipo)) {
			return organismos.get(tipo).size();
		}
		return 0;
		
	}
	
	public int getNumOrganismosTotal() {
		int cont = 0;
		for(TipoOrganismo tipo: TipoOrganismo.values()) {
			cont+=organismos.get(tipo).size();
		}
		return cont;
	}
	
	public boolean vive() {
		
		if(getAgua()<50.0 || getNumOrganismosTotal() == 0) {
			return false;
		}
		
		return true;
		
	}

	@Override
	public String toString() {
		return "Ecosistema [agua=" + agua + ", clima=" + clima +
				", plantas=" + getNumOrganismosPorTipo(TipoOrganismo.PLANTA) 
				+", Carnivoro=" + getNumOrganismosPorTipo(TipoOrganismo.CARNIVORO)
				+", Herbivoro=" + getNumOrganismosPorTipo(TipoOrganismo.HERBIVORO)
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(agua, clima, organismos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ecosistema other = (Ecosistema) obj;
		return Double.doubleToLongBits(agua) == Double.doubleToLongBits(other.agua) && clima == other.clima
				&& Objects.equals(organismos, other.organismos);
	}
	
	/**
	 * Programa que simula el paso de un año en el ecosistema:
	- Añadir 1 año de edad a todos los organismos.
	- Si los organismos han llegado a su edad mínima para reproducirse, crear tantos nuevos organismos
	como indique el atributo reproducción (clones de Organismo, salvo edad = 0).
	- Si los organismos han llegado a su edad máxima, eliminarlos.
	- Por cada planta, reducir del total de agua de ecosistema el agua que necesita esa planta. Si la
	cantidad de agua disponible no es suficiente, la planta muere (eliminamos la planta del ecosistema).
	- Por cada herbívoro, eliminar del ecosistema una instancia de cada Planta que tenga en su atributo
	alimentación. Si no es posible que un herbívoro complete su alimentación, muere (eliminamos el
	herbívoro del ecosistema).
	- Por cada carnívoro, eliminar del ecosistema una (solo una) instancia de las que hay en su atributo
	alimentación. Si no es posible que un carnívoro complete su alimentación, muere (eliminamos el
	carnívoro del ecosistema).
	 */
	public void simularAnyo() {
		
		
		//Añadir 1 año de edad a todos los organismos
		for(TipoOrganismo tipoOrg: organismos.keySet()) {
			ArrayList<Organismo> nuevos = new ArrayList<>();
			for(Organismo organismo: organismos.get(tipoOrg)) {
				organismo.setEdad(organismo.getEdad()+1);
				//Si los organismos han llegado a su edad mínima para reproducirse, crear tantos nuevos organismos
				//como indique el atributo reproducción (clones de Organismo, salvo edad = 0).
				if(organismo.getEdad()>=organismo.getEdadMin()) {
					for(int i = 0; i<organismo.getReproduccion();i++) {
						Organismo orgNuevo = null;
						switch(tipoOrg) {
							case CARNIVORO:
								orgNuevo = new Carnivoro((Carnivoro) organismo);
								break;
							case HERBIVORO:
								orgNuevo = new Herbivoro((Herbivoro) organismo);
								break;
							case PLANTA:
								orgNuevo = new Planta((Planta) organismo);
								break;
							default:
								System.err.println("Tipo de organismo incorrecto");
								break;							
						}
						organismo.setEdad(0);
						nuevos.add(orgNuevo);						
					}
				}
					
			}
			organismos.get(tipoOrg).addAll(nuevos);
		}
		
		
		//Por cada planta, reducir del total de agua de ecosistema el agua que necesita esa planta. Si la
		//cantidad de agua disponible no es suficiente, la planta muere (eliminamos la planta del ecosistema).
		ArrayList<Organismo> borrarOrg = new ArrayList<>();
		for(Organismo organismo: organismos.get(TipoOrganismo.PLANTA)) {
			if((agua-((Planta)organismo).getAgua())<0) {
				System.out.println("El organismo "+organismo+"ha muerto por falta de agua");
				borrarOrg.add(organismo);
			}else {
				agua-= ((Planta)organismo).getAgua();
			}
		}
		organismos.get(TipoOrganismo.PLANTA).removeAll(borrarOrg);
		
		//Por cada herbívoro, eliminar del ecosistema una instancia de cada Planta que tenga en su atributo
		//alimentación. Si no es posible que un herbívoro complete su alimentación, muere (eliminamos el
		//herbívoro del ecosistema).
		borrarOrg.clear();
		ArrayList<Organismo> borrarComida = new ArrayList<>();
		for(Organismo organismo: organismos.get(TipoOrganismo.HERBIVORO)) {
			for (Organismo alim: ((Herbivoro)organismo).getAlimentacion()) {
				boolean encontrado = false;
				for (int i = 0;i<organismos.get(TipoOrganismo.PLANTA).size();i++) {
					if(alim.getEspecie().equals(organismos.get(TipoOrganismo.PLANTA).get(i).getEspecie())) {
						encontrado = true;
						borrarComida.add(organismos.get(TipoOrganismo.PLANTA).get(i));
						System.out.println("Un/a "+organismo.getEspecie()+ " se ha comido un/a "+alim.getEspecie());
					}
				}
				if(!encontrado) {
					borrarOrg.add(organismo);
					System.out.println("El organismo "+organismo+" ha muerto por inanición");
					break;
				}
			}
		}
		organismos.get(TipoOrganismo.HERBIVORO).removeAll(borrarOrg);
		organismos.get(TipoOrganismo.PLANTA).removeAll(borrarComida);
		
		//Por cada carnívoro, eliminar del ecosistema una (solo una) instancia de las que hay en su atributo
		//alimentación. Si no es posible que un carnívoro complete su alimentación, muere (eliminamos el
		//carnívoro del ecosistema).
		borrarOrg.clear();
		borrarComida.clear();
		for(Organismo organismo: organismos.get(TipoOrganismo.CARNIVORO)) {
			int aleat = (int) (Math.random() * ((Carnivoro)organismo).getAlimentacion().size());
			String alim =  ((Carnivoro)organismo).getAlimentacion().get(aleat).getEspecie();
			boolean encontrado = false;
			for (int i = 0;i<organismos.get(TipoOrganismo.CARNIVORO).size();i++) {
				if(alim.equals(organismos.get(TipoOrganismo.CARNIVORO).get(i).getEspecie())) {
					encontrado = true;
					borrarComida.add(organismos.get(TipoOrganismo.CARNIVORO).get(i));
					System.out.println("Un/a "+organismo.getEspecie()+ " se ha comido un/a "+alim);
				}
			}
			if(!encontrado){
				System.out.println("El organismo "+organismo+" ha puesto por inanición");
				borrarOrg.add(organismo);
			}
		}
		organismos.get(TipoOrganismo.CARNIVORO).removeAll(borrarOrg);
		organismos.get(TipoOrganismo.CARNIVORO).removeAll(borrarComida);
	}
}

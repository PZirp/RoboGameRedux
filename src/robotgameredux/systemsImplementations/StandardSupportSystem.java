package robotgameredux.systemsImplementations;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.SupportCommandInterface;
import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IActorManager;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.tools.UsableTool;

public class StandardSupportSystem implements SupportSystem, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 730914482322382664L;

	/**
	 * Classe che implementa una modalità per eseguire un comando di interazione per classi di attacco.
	 * Quando un attore di supporto viene creato, è possibile aggiungere questo sistema se si desidera 
	 * che il proprio attore abbia la capacità di interagire con l'ambiente in veste di classe di attacco.
	 * Questo sistema permette di interagira con l'ambiente in tre modi:
	 * 1) Distruggere un ostacolo, se l'attore ha abbastanza forza ed lo spostamento è consentito
	 * 2) Prendere oggetti di tipo Weapon dalla stazione di ricarica
	 * 3) Ricaricare l'energia di un attore tramite stazione di ricarica
	 * In tutti e tre i casi l'attore deve essere adiacente all'obiettivo. In questa implementazione,
	 * ogni azione ha un costo fisso. 
	 * 
	 * Questa classe implementa l'interfaccia InteractionSystem definendo comportamenti per classi di attacco.
	 * 
	 * @author Paolo Zirpoli
	 */
	
	/**
	 * Riferimento all'actor manager
	 * @param rf
	 * 		l'actor manager corrente
	 */
	
	public StandardSupportSystem(IActorManager rf) {
		this.actorsManager = rf;
	}
	
	/**
	 * Metodo che esegue un comando di supporto. Prende come argomento un
	 * SupportCommandInterface generico, che può incapsulare un qualunque tipo di
	 * entità "abilitata" al supporto. Per prima cosa controlla che l'attore che vuole
	 * eseguire l'azione abbia sufficiente energia per utilizzare l'oggetto scelto. 
	 * Se così non dovesse essere, lancia una InsufficientEnergiException. Poi controlla che il target scelto 
	 * sia valido tramite un actorManager. In seguito controlla che entrambi gli attori 
	 * appartengano alla stessa fazione. Se una di queste due condizione non fosse rispettata,
	 * lancia una InvalidTargetException.
	 * Infine, utilizza il metodo use() dell'UsableTool scelto dando come parametro il target
	 * ricevuto. Rimuove il tool usato e rimuove l'energia in case al costo dell'utilizzo.
	 * 
	 * @param command,
	 *            interfaccia che rappresenta il comando di supporto
	 * @return true se l'azione di supporto ha successo, false altrimenti
	 * @throws InvalidTargetException
	 * 			Se il target non è valido
	 * @throws InsufficientEnergyException
	 * 			Se non c'è energia sufficiente per eseguire il comando
	 */

	@Override
	public <T> Boolean execute(SupportCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {
		Coordinates target = command.getTarget();
		UsableTool tool = command.getActiveTool();

		if (command.getEnergy() < tool.getCost()) {
			throw new InsufficientEnergyException(command, tool.getCost());
		}

		TargetInterface<?> targeted = actorsManager.getTarget(target);
		if (targeted == null || targeted.getFaction() != command.getFaction()) {
			throw new InvalidTargetException(command);
		}

		tool.use(targeted);
		command.removeUsedTool(tool);
		command.removeEnergy(tool.getCost());
		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[ActorManager: " + actorsManager.toString() + "]";
	}

	@Override
	public StandardSupportSystem clone() {
		try {
			StandardSupportSystem clone = (StandardSupportSystem) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		StandardSupportSystem other = (StandardSupportSystem) otherObject;
		return actorsManager.equals(other.actorsManager);
	}

	private IActorManager actorsManager;

}

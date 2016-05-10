package robotgameredux.input;

public enum RobotStates {

	INACTIVE, // Generica
	ACTIVE, // Generica
	MOVING,  //	Generica
	ATTACKING, // Attaccante
	DO_NOTHING, // Generica
	TURN_OVER, // Generica
	DESTROY_OBSTACLE, // Attaccante
	PUSH_OBSTACLE, // Attaccante
	USE_OBJECT; // Supporto o Generica (dipende da come implemento l'uso degli oggetti di cura/munizioni dati dal supporto) potrei aver bisogno di uno sttao GIVE_OBJECT
	//Se proprio vogliamo essere sicuri che un robot combattente non possa finire in un stato del robot di supporto e viceversa,
	//aggiungere un else alla fine di tutti gli stati validi e lanciare un'eccezione "InvalidStateException" da lì, ma mi pare esagerato

}

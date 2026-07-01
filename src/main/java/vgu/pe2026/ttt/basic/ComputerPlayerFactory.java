package vgu.pe2026.ttt.basic;

public class ComputerPlayerFactory extends PlayerFactory {

	@Override
	public Player createPlayer() {
		return new ComputerPlayer();
	}
}

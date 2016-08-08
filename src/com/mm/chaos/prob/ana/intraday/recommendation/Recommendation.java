package com.mm.chaos.prob.ana.intraday.recommendation;

public class Recommendation 
{
	
	Direction direction = Direction.NONE;
	Level level = Level.NONE;
	PositionStatus positionStatus = PositionStatus.NONE;
	Double entryPoint = 0.0;

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public PositionStatus getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(PositionStatus positionStatus) {
		this.positionStatus = positionStatus;
	}

	
	
	public Double getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(Double entryPoint) {
		this.entryPoint = entryPoint;
	}

	@Override
	public String toString() {
		return "Recommendation [direction=" + direction + ", level=" + level + ", positionStatus=" + positionStatus
				+ ", entryPoint=" + entryPoint + "]";
	}

	
	

}

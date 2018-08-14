package com.dj.SpringBoot.Domain;

import java.io.Serializable;
import java.util.List;

public class Water implements Serializable{
	private static final long serialVersionUID = 6392376146163510146L;
    public int id;
    public List<Double> runoff;

    public Water(int id, List<Double> runoff) 
    {
        this.id = id;
        this.runoff = runoff;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Double> getRunoff() {
		return runoff;
	}

	public void setRunoff(List<Double> runoff) {
		this.runoff = runoff;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}

package sig.skills;

import sig.Craft;
import sig.Skill;

public class Observe extends Skill {
	public Observe(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
	}
}

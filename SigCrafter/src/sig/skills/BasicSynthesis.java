package sig.skills;

import sig.Craft;
import sig.Skill;

public class BasicSynthesis extends Skill {

	public BasicSynthesis(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.craft_progress += c.base_progress * c.progress_mult * 1.2;
		c.craft_durability -= 10 * c.durability_mult;
	}

}

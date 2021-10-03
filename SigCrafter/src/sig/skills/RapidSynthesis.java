package sig.skills;

import sig.Craft;
import sig.Skill;

public class RapidSynthesis extends Skill {

	public RapidSynthesis(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public RapidSynthesis(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.craft_progress += c.base_progress * c.progress_mult * 2.5;
		c.craft_durability -= 10 * c.durability_mult;
	}

}

package me.adelemphii.molynsi.mutations;

public enum Mutation {

    PREVIOUS_LIFE_SKILL(new PreviousLifeSkill());
    // TODO: Idk what I'm doing at all
    MutationBase mutationBase;
    Mutation(MutationBase mutationBase) {
        this.mutationBase = mutationBase;
    }

    public MutationBase getMutationBase() {
        return mutationBase;
    }

}

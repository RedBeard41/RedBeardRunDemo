package Helpers;

import com.badlogic.ashley.core.ComponentMapper;

import Components.AnimationComponent;
import Components.BodyComponent;
import Components.CollisionComponent;
import Components.DataComponent;
import Components.EnemyComponent;
import Components.InteractiveComponent;
import Components.NPCComponent;
import Components.ParticleEffectComponent;
import Components.PlayerComponent;
import Components.PlayerControlComponent;
import Components.PositionComponent;
import Components.ProjectileComponent;
import Components.RenderableComponent;
import Components.SoundComponent;
import Components.StateComponent;
import Components.SteeringComponent;
import Components.TextureComponent;
import Components.TouchComponent;
import Components.TypeComponent;

public class Mappers {
    public static final ComponentMapper<AnimationComponent> animation =
            ComponentMapper.getFor(AnimationComponent.class);

    public static final ComponentMapper<BodyComponent> body =
            ComponentMapper.getFor(BodyComponent.class);

    public static final ComponentMapper<CollisionComponent> collision =
            ComponentMapper.getFor(CollisionComponent.class);


    public static final ComponentMapper<DataComponent> data =
            ComponentMapper.getFor(DataComponent.class);


    public static final ComponentMapper<StateComponent> state =
            ComponentMapper.getFor(StateComponent.class);


    public static final ComponentMapper<EnemyComponent> enemy =
            ComponentMapper.getFor(EnemyComponent.class);


    public static final ComponentMapper<NPCComponent> npc =
            ComponentMapper.getFor(NPCComponent.class);


    public static final ComponentMapper<PlayerComponent> player =
            ComponentMapper.getFor(PlayerComponent.class);


    public static final ComponentMapper<PlayerControlComponent> control =
            ComponentMapper.getFor(PlayerControlComponent.class);


    public static final ComponentMapper<RenderableComponent> render =
            ComponentMapper.getFor(RenderableComponent.class);


    public static final ComponentMapper<SteeringComponent> steering =
            ComponentMapper.getFor(SteeringComponent.class);


    public static final ComponentMapper<TextureComponent> texture =
            ComponentMapper.getFor(TextureComponent.class);


    public static final ComponentMapper<TypeComponent> type =
            ComponentMapper.getFor(TypeComponent.class);


    public static final ComponentMapper<PositionComponent> position =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<TouchComponent> touch =
            ComponentMapper.getFor(TouchComponent.class);

    public static final ComponentMapper<InteractiveComponent> interactive =
            ComponentMapper.getFor(InteractiveComponent.class);

    public static final ComponentMapper<SoundComponent> sound =
            ComponentMapper.getFor(SoundComponent.class);

    public static final ComponentMapper<ProjectileComponent> projectile =
            ComponentMapper.getFor(ProjectileComponent.class);

    public static final ComponentMapper<ParticleEffectComponent> particle =
            ComponentMapper.getFor(ParticleEffectComponent.class);




}

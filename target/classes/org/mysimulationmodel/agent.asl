force(3.0).
canceldetour(0.0).

!main.

//main plan
+!main <-
    generic/print( "Pedestrian's Name", MyName );
    route/set/calculate;
    !walk
.

//when car get their free-flow trajectories, they start walking
+!walk : >>( force(S), generic/type/isnumeric (S) && S == 3.0 )
    <-
        calculate/next/position;
        generic/print( "walking", MyName );
        !walk
.

//when pedestrian reach their destination or sub-destination
+!reached/destination( G )
    <-
        generic/print("reached destination or sub-destination",MyName )
.
//1
+!force/decelerate : >>( force(S), generic/type/isnumeric (S) && S == 1.0 )
     <-
      pedestrian/stop/moving;
      generic/print( "decelerating", MyName );
      !force/decelerate;
      !walk
 .
//0
+!force/accelerate : >>( force(S), generic/type/isnumeric (S) && S == 0.0 )
 <-
      calculate/next/position;
      generic/print( "accelerating ggggggggggggggggggggg", MyName );
      !force/accelerate;
      !walk
.
//2
//go from behind the car
+!force/deviate : >>( force(S), generic/type/isnumeric (S) && S == 2.0 )
 <-
      deviate/movement;
      calculate/next/position;
      generic/print( "deviating", MyName );
      !!update/belief (0.0);
      !force/accelerate;
      !walk
.

+!force/deviate/withoutplaying
 <-
      deviate/movement;
      calculate/next/position;
      generic/print( "deviating", MyName );
      !!update/belief (0.0);
      !force/accelerate;
      !walk
.



+!freely/moving <-
//generic/print( "freely moving", MyName );
!!update/belief(1.0);
!force/decelerate
.

+!update/belief (G) <-
 >>force(S); -force(S); +force(G)
 //;
 //generic/print( "Belief updated",S, G, MyName )
 .

+!update/detour/belief
: >>( canceldetour(S), generic/type/isnumeric (S) && S < 1.0 ) <-
 -canceldetour(S); +canceldetour(1.0); generic/print( "check", MyName );
 !!update/detour/belief
 : >>( canceldetour(S), generic/type/isnumeric (S) && S == 1.0 ) <-
 !!cancel/detour
.

+!cancel/detour
    : >>( canceldetour(S), generic/type/isnumeric (S) && S > 0.0 )
    <-
    F = S - 1; - canceldetour(S); + canceldetour(F); !cancel/detour

    : >>( canceldetour(S), generic/type/isnumeric (S) && S == 0.0 )
    <-  generic/print( "canceling Detouring", MyName ); cancel/detour
.

+!delete/plan (P)
<-
agent/removeplan( "+!", "P")
.




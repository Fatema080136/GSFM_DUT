force(3.0).

!main.

//main plan
+!main <-
    generic/print( "Car's Name", MyName );
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

+!freely/moving <-
//calculate/next/position;
//generic/print( "freely moving", MyName );
!!update/belief(1.0);
!force/decelerate.

//when pedestrian reach their sub-destination
+!reached/sub-destination( G )
    <-
        generic/print( "reached sub-destination")
.

// force(1)
+!force/decelerate : >>( force(S), generic/type/isnumeric (S) && S == 1.0 )
     <-
     car/stop/moving;
     generic/print( "decelerating", MyName );
     !force/decelerate;
     !walk
 .

// force(0)
+!force/accelerate : >>( force(S), generic/type/isnumeric (S) && S == 0.0 )
 <-
      calculate/next/position;
      //check/reachability/of/intermediate/goal;
      generic/print( "accelerating", MyName );
      !force/accelerate;
      !walk
.

-!force/accelerate : >>( force(S), generic/type/isnumeric (S) && S == 0.0 )
 <-
        car/stop/moving;
        generic/print( "problem in accelaration", MyName );
        !update/belief (1.0);
        !force/decelerate;
        !walk
.

+!update/belief (G) <-
 >>force(S); -force(S); +force(G)
 //;
 //generic/print( "Belief updated",S, G, MyName )
 .

+!delete/plan (P)
<-
   agent/removeplan( "+!", P)
.

//when pedestrian reach their destination
+!reached/destination( G )
    <-
        //generic/print( MyName, "reached destination", G );
        !!update/belief (0)
.




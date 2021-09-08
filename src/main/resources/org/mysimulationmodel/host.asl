!main.

+!main <-
    generic/print("....Host....")
    ;
    !perceive
.

+!perceive <-
   host/perceive;
   host/trigger/otheragents;
   !perceive
.
-!perceive <-
     generic/print("Problem");
     //host/again/trigger;
     !perceive.
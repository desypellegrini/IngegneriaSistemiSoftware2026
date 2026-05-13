%====================================================================================
% firefly description   
%====================================================================================
dispatch( cellstate, cellstate(X,Y,COLOR) ). %commute cell state
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8040").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( creator, ctxfirefly, "it.unibo.creator.Creator").
 static(creator).
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 

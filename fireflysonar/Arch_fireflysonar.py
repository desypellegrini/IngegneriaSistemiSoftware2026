### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('fireflysonarArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxfirefly', graph_attr=nodeattr):
          creator=Custom('creator','./qakicons/symActorWithobjSmall.png')
          sonarmock=Custom('sonarmock','./qakicons/symActorWithobjSmall.png')
          controller=Custom('controller','./qakicons/symActorWithobjSmall.png')
          firefly=Custom('firefly','./qakicons/symActorDynamicWithobj.png')
     with Cluster('ctxgrid', graph_attr=nodeattr):
          griddisplay=Custom('griddisplay(ext)','./qakicons/externalQActor.png')
     sonarmock >> Edge( label='sonardata', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='sonardata', **evattr, decorate='true', fontcolor='darkgreen') >> controller
     controller >> Edge( label='sync', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     controller >> Edge( label='desync', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='sync', **evattr, decorate='true', fontcolor='darkgreen') >> firefly
     sys >> Edge( label='desync', **evattr, decorate='true', fontcolor='darkgreen') >> firefly
     firefly >> Edge(color='blue', style='solid',  decorate='true', label='<cellstate &nbsp; >',  fontcolor='blue') >> griddisplay
diag

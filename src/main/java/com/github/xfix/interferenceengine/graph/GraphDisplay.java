/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xfix.interferenceengine.graph;

import com.github.xfix.interferenceengine.expression.Expression;
import com.github.xfix.interferenceengine.expression.Variable;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

/**
 *
 * @author Konrad Borowski <xfix at protonmail.com>
 */
public class GraphDisplay extends JApplet {

    private JGraphXAdapter<Variable, DefaultEdge> graphAdapter;

    private GraphDisplay() {
    }

    public static void draw(ArrayList<Variable> variables) {
        GraphDisplay graph = new GraphDisplay();
        graph.init(variables);

        JFrame frame = new JFrame();
        frame.getContentPane().add(graph);
        frame.setTitle("Graf powiązań");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void init(ArrayList<Variable> variables) {
        // create a JGraphT graph
        ListenableGraph<Variable, DefaultEdge> g
                = new ListenableDirectedGraph<>(
                        DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
        graphAdapter = new JGraphXAdapter<>(g);

        getContentPane().add(new mxGraphComponent(graphAdapter));

        graphAdapter.getModel().beginUpdate();
        try {
            for (Variable variable : variables) {
                g.addVertex(variable);
            }
            for (Variable variable : variables) {
                boolean[] booleanValues = {true, false};
                for (boolean booleanValue : booleanValues) {
                    Optional<Expression> expression = variable.getExpression(booleanValue);
                    if (expression.isPresent()) {
                        for (Variable dependency : expression.get().getDependencies()) {
                            g.addEdge(variable, dependency);
                        }
                    }
                }
            }
        } finally {
            graphAdapter.getModel().endUpdate();
        }

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);

        layout.execute(graphAdapter.getDefaultParent());
    }

}

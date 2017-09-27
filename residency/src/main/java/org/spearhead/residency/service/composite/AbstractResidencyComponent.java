package org.spearhead.residency.service.composite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public abstract class AbstractResidencyComponent implements ResidencyComponent {
    protected List<ResidencyComponent> components = new ArrayList<>();
    protected ResidencyComponentType componentType;

    public AbstractResidencyComponent(ResidencyComponentType componentType) {
        this.componentType = componentType;
    }

    @Override
    public ResidencyComponent addChildComponent(ResidencyComponent component) {
        int index = components.indexOf(component);
        if (index == -1) {
            components.add(component);
        } else {
            component = components.get(index);
        }

        return component;
    }

    @Override
    public List<ResidencyComponent> getChildComponents() {
        return components;
    }

    @Override
    public int getResidentCount() {
        int residentCount = 0;
        for (ResidencyComponent component : components) {
            residentCount += component.getResidentCount();
        }

        return residentCount;
    }

    @Override
    public ResidencyComponentType getComponentType() {
        return componentType;
    }

    @Override
    public Iterator<ResidencyComponent> iterator() {
        return new DepthFirstIterator();
    }

    private class DepthFirstIterator implements Iterator<ResidencyComponent> {
        private ResidencyComponent next = AbstractResidencyComponent.this;
        private Stack<Iterator<ResidencyComponent>> iteratorStack = new Stack<>();

        {
            ArrayList<ResidencyComponent> list = new ArrayList<>();
            list.add(next);
            iteratorStack.push(list.iterator());
        }

        @Override
        public boolean hasNext() {
            return !iteratorStack.isEmpty();
        }

        @Override
        public ResidencyComponent next() {
            ResidencyComponent current = iteratorStack.peek().next();
            iteratorStack.push(current.getChildComponents().iterator());

            while (!iteratorStack.isEmpty() && !iteratorStack.peek().hasNext()) {
                iteratorStack.pop();
            }

//            next = iteratorStack.isEmpty() ? null : iteratorStack.peek().next();
//            if (!this.next.getChildComponents().isEmpty()) {
//                iteratorStack.push(this.next.getChildComponents().iterator());
//            }

            return current;
        }
    }
}

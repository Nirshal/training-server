package com.nirshal.util.containers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Index implements Comparable<Index> {

    private Integer x;
    private Integer y;

    public int compareTo(Index index)
    {
        return index.x.equals(x) ?
        Integer.compare(index.y, y)
        :
        Integer.compare(index.x, x);
    }

    public int hashCode()
    {
//        final int PRIME = 31;
//        int result = 1;
//        result = PRIME * result + (int) (x ^ (x >>> 32));
//        result = PRIME * result + (int) (y ^ (y >>> 32));
        String key = x.toString() + "_" + y.toString();

        return key.hashCode();
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Index other = (Index) obj;

        if (!x.equals(other.x)) return false;
        if (!y.equals(other.y)) return false;

        return true;
    }
}

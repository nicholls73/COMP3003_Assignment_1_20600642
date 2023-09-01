/*
    FILE:       ArenaListener
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   N/A
    PURPOSE:    Is the interface used for the listener in the observer pattern.
    NOTES:      Was provided by the lecturer.
*/

package edu.curtin.saed.assignment1;

/**
 * Represents an event handler for when the arena is clicked.
 */
public interface ArenaListener
{
    void squareClicked(int x, int y);
}

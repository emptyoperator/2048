package org.game;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.game.Constants.BOARD_SIZE;
import static org.game.Constants.BORDER_SIZE;
import static org.game.Constants.SCALE_DURATION;
import static org.game.Constants.TILE_SIZE;

public class Tiles extends Pane {
    private static final Random RANDOM = new Random();
    private final int size;
    private final List<List<Tile>> tiles;

    public Tiles() {
        this(BOARD_SIZE);
    }

    private Tiles(int size) {
        this.size = size;
        tiles = Stream.generate(() -> Stream.<Tile>generate(() -> null).limit(size).collect(toList())).limit(size).collect(toList());
        addTile();
        addTile();
        setOnKeyPressed(key -> Optional.ofNullable(Direction.fromKeyCode(key.getCode())).ifPresent(this::move));
    }

    public void move(Direction direction) {
        this.new TileIterator(direction).forEach();
    }

    private void addTile() {
        record Pair(int i, int j) {
        }
        Stream.generate(() -> new Pair(RANDOM.nextInt(BOARD_SIZE), RANDOM.nextInt(BOARD_SIZE)))
                .filter(pair -> get(pair.i, pair.j).isEmpty())
                .findAny()
                .ifPresent(pair -> addTile(pair.i, pair.j));
    }

    private void addTile(int i, int j) {
        Tile tile = new Tile(j * TILE_SIZE + (j + 1) * BORDER_SIZE, i * TILE_SIZE + (i + 1) * BORDER_SIZE);
        getChildren().add(tile);
        set(i, j, tile);
        ScaleTransition transition = new ScaleTransition(SCALE_DURATION, tile);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
    }

    private Optional<Tile> get(int i, int j) {
        return Optional.ofNullable(tiles.get(i).get(j));
    }

    private void set(int i, int j, Tile element) {
        tiles.get(i).set(j, element);
    }

    private void remove(int i, int j) {
        set(i, j, null);
    }

    private class TileIterator {
        final Direction direction;
        final Range i = new Range(0, size);
        final Range j = new Range(0, size);
        final Set<Tile> merged = new HashSet<>();
        final ParallelTransition transition = new ParallelTransition();

        TileIterator(Direction direction) {
            this.direction = direction;
            switch (direction) {
                case DOWN -> i.reverse();
                case RIGHT -> j.reverse();
            }
        }

        void forEach() {
            i.forEachOrdered(i -> j.forEachOrdered(j -> handle(i, j)));
            if (!transition.getChildren().isEmpty()) {
                transition.setOnFinished(e -> addTile());
                transition.play();
            }
        }

        void handle(int i, int j) {
            get(i, j).ifPresent(current -> {
                IndexIterator iterator = new IndexIterator(i, j, current.value());
                int distance = 0;
                Tile next = null;
                while (iterator.hasNext()) {
                    distance++;
                    next = iterator.next();
                }
                if (distance > 0) {
                    if (nonNull(next) && current.value() == next.value() && !merged.contains(next)) {
                        remove(i, j);
                        next.promote();
                        merged.add(next);
                        current.toBack();
                        Animation animation = current.getMoveAnimation(direction, distance);
                        Tile finalNext = next;
                        animation.setOnFinished(e -> {
                            getChildren().remove(current);
                            finalNext.getPromoteAnimation().play();
                        });
                        transition.getChildren().add(animation);
                    } else if (isNull(next)) {
                        IndexIterator.Index index = iterator.index;
                        remove(i, j);
                        set(index.i, index.j, current);
                        transition.getChildren().add(current.getMoveAnimation(direction, distance));
                    }
                }
            });
        }

        class IndexIterator {
            Index index;
            final int value;

            IndexIterator(int i, int j, int value) {
                this.index = new Index(i, j);
                this.value = value;
            }

            boolean hasNext() {
                Index next = index.next();
                if (!next.exists()) {
                    return false;
                }
                Optional<Tile> tile = next.get();
                boolean hasSameValue = tile.map(t -> t.value() == value && !merged.contains(t)).orElse(false);
                return tile.isEmpty() || hasSameValue;
            }

            Tile next() {
                index = index.next();
                return index.get().orElse(null);
            }

            class Index {
                final int i;
                final int j;

                Index(int i, int j) {
                    this.i = i;
                    this.j = j;
                }

                Optional<Tile> get() {
                    return Tiles.this.get(i, j);
                }

                Index next() {
                    int nextI = i;
                    int nextJ = j;
                    switch (direction) {
                        case UP -> nextI--;
                        case DOWN -> nextI++;
                        case LEFT -> nextJ--;
                        case RIGHT -> nextJ++;
                    }
                    return new Index(nextI, nextJ);
                }

                boolean exists() {
                    return i >= 0 && i < size && j >= 0 && j < size;
                }
            }
        }

        static class Range {
            int start;
            int end;
            boolean reversed = false;

            Range(int start, int end) {
                this.start = start;
                this.end = end;
            }

            void forEachOrdered(IntConsumer action) {
                if (reversed) {
                    IntStream.iterate(end - 1, i -> i >= start, i -> i - 1).forEachOrdered(action);
                } else {
                    IntStream.iterate(start, i -> i < end, i -> i + 1).forEachOrdered(action);
                }
            }

            void reverse() {
                reversed = true;
            }
        }
    }
}

package org.staticData;

import lombok.extern.slf4j.Slf4j;
import org.automation.base.mobile.enums.DividedList;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class CollectionsExtensions {

    /**
     * List<Integer> items = Arrays.asList(1,3,6,8);
     * Stream<Integer> stream = list.parallelStream();
     * stream.collect(reverseList()).forEach(System.out::println);
     * @param <T> any type from stream type
     * @return fix list of..
     */
    public <T> Collector<T,?, Stream<T>> iterateList(boolean runInReverse) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            if (runInReverse) Collections.reverse(list);
            else Collections.addAll(list);
            return list.stream();
        });
    }

    /**
     * List<Integer> items = Arrays.asList(1,3,6,8);
     * Iterator<Integer> reverseList = reverseLinkedList.parallelStream();
     * reverseList.forEachRemaining(System.out::println);
     * @param streamItems Stream<T> streamItems
     * @param <T> any type from stream type
     * @return fix list of..
     */
    public <T> Iterator<T> reverseLinkedList(Stream<T> streamItems) {
        return streamItems.collect(
                Collectors.toCollection(LinkedList::new))
                .descendingIterator();
    }

    /**
     * List<Integer> items = Arrays.asList(1,3,6,8);
     * Stream<Integer> reverse = list.parallelStream();
     * reverseDequeList(reverse).forEach(System.out::println);
     * @param streamItems Stream<T> streamItems
     * @param <T> any type from stream type
     * @return fix list of..
     */
    public <T> Stream<T> reverseDequeList(Stream<T> streamItems) {
        return streamItems.collect(Collector.of((
                Supplier<ArrayDeque<T>>) ArrayDeque::new,
                ArrayDeque::addFirst, (a, b) -> {
            b.addAll(a);
            return b;
        })).stream();
    }

    public <T, V> boolean verifyIsTwoListEquals(List<T> arr1, List<V> arr2) {
        try {
            return new HashSet<>(arr1).equals(new HashSet<>(arr2));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * List<Integer> items = Arrays.asList(1,3,6,8);
     * Stream<Integer> reverse = list.parallelStream();
     * reverseArrayList(reverse).forEach(System.out::println);
     * @param streamItems Stream<T> streamItems
     * @param <T> any type from stream type
     * @return fix list of..
     */
    public <T> Collector<T, List<T>, List<T>> reverseArrayList(Stream<T> streamItems) {
        return Collector.of(ArrayList::new,
                List::add,
                (a,b) -> { a.addAll(b); return a; },
                list -> { Collections.reverse(list); return list; });
    }

    /**
     * createListFromString from single string
     * @param item the string its self
     * @param splitRegex ...
     * @return ...
     */
    public List<String> createListFromString(String item, String splitRegex) {
        List<String> collect = new ArrayList<>();
        String [] stringSpilt = item.split(splitRegex);

        for (String str : stringSpilt) {
            log.info("add " + str + " from string");
            collect.add(str);
        }

        return collect;
    }


    /**
     * Returns a random item in a string list.
     * If the list id empty returns an empty string
     * @param givenList a given String list
     * @return returns a random item in a string list
     */
    public String getRandomItemFromAList(List<String> givenList) {
        if (givenList.size() == 0) {
            String listName = givenList.toString() != null ? givenList.toString() : "";
            log.info("The: " + listName + " list is empty");
            return "";
        }

        return givenList.get(new Random().nextInt(givenList.size()));
    }

    /**
     * @param list1 big list of strings
     * @param list2 list 1 will verify is there any object from list 2 or not not by contains param
     * @return true/false
     */
    public boolean isListContainsAnotherListMembers(List<String> list1, List<String> list2, boolean fail) {
        List<String> find = list1
                .stream()
                .filter(list2::contains)
                .peek(e -> log.info("items in the list" + e))
                .collect(Collectors.toList());

        return !find.isEmpty();
    }

    /**
     * @param contains to set the verification
     * @param list1 big list of strings
     * @param list2 list 1 will verify is there any object from list 2 or not not by contains param
     * @return true/false
     */
    public <T,V> boolean isListContainsAnotherListMember(boolean contains, List<T> list1, List<V> list2, boolean fail) {
        if (contains) return !Collections.disjoint(list1, list2);
        return Collections.disjoint(list1, list2);
    }

    /**
     * optionalString file
     * @param list of strings
     * @param filterFromList by predicate option
     * @param backUpStringReturnOption if cannot find str element
     * @return the filter predicate or else option
     */
    public String filterStringBy(List<String> list, String filterFromList, String backUpStringReturnOption) {
        return list.stream()
                .filter(str -> !str.isEmpty())
                .filter(text -> text.contains(filterFromList))
                .findAny()
                .orElse(backUpStringReturnOption);
    }

    /**
     * optionalString file
     * @param list of strings
     * @param filterFromList by predicate option
     * @return the filter predicate or else option
     */
    public Optional<String> filterStringBy(List<String> list, Predicate<String> filterFromList) {
        return list.stream()
                .filter(str -> !str.isEmpty())
                .filter(filterFromList)
                .findAny();
    }

    /**
     * fromManyToManyZipTwoLists
     * @param firstList List<T> firstList
     * @param secondList List<T> secondList
     * @param <T> any obj that will enter between collect first list and the second
     * @return fix list
     */
    public <T> List<T> zipListWithSingle(List<T> firstList, T secondList) {
        List<T> collector = new ArrayList<>();
        try {
            collector.addAll(firstList);
            if (secondList != null) collector.add(secondList);
            return collector;
        } catch (Exception e) {
            log.error("prepareListFindElements error" + e.getMessage());
            return collector;
        }
    }

    /**
     * split list to two parts from the middle of the list
     * @param dividedList
     * FROM_UPPER_TO_MIDDLE,
     * FROM_MIDDLE_TO_LOWER,
     * ALL_COLLECTION
     * @param elementList List<T> elementList
     * @param <T> Generic type of list
     * @return
     */
    public <T> List<T> partitionsListBy(DividedList dividedList, List<T> elementList) {
        List<List<T>> results;

        List<T> result = new ArrayList<>();
        int size = elementList.size(), parts = 2, partSize = size / parts;

        try {
            results = IntStream.range(0, parts)
                    .parallel()
                    .mapToObj(i -> elementList.subList(i * partSize, (i + 1) * partSize))
                    .collect(Collectors.toList());
            if (dividedList == DividedList.FROM_UPPER_TO_MIDDLE) {
                return results.get(0);
            } else if (dividedList == DividedList.FROM_MIDDLE_TO_LOWER) {
                return results.get(1);
            } else return elementList;
        } catch (Exception e) {
            //
        }

        return result;
    }

    public <T> List<T> subListList(DividedList searchList, List<T> elementList) {
        List<T> dividedList;

        if (searchList == DividedList.ALL_COLLECTION) {
            return elementList;

        } else if (searchList == DividedList.FROM_UPPER_TO_MIDDLE) {
            int size = elementList.size();
            dividedList = elementList.subList(0, (size) / 2);
            return !dividedList.isEmpty() ? dividedList : elementList;

        } else if (searchList == DividedList.FROM_MIDDLE_TO_LOWER) {
            int size = elementList.size();
            dividedList = elementList.subList((size) / 2, size);
            return !dividedList.isEmpty() ? dividedList : elementList;

        } else log.error("subListList provide valid SearchList params options");

        return new ArrayList<>();
    }

    public <T>List<T> getLastElementsFromList(List<T> oldList, List<T> newList, int getLast) {
        try {
            return newList.subList(Math.max(oldList.size() -getLast, 0), oldList.size());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private boolean isListEqualsToAnotherList(List<String> listOne, List<String> listTwo) {
        return new HashSet<>(listOne).equals(new HashSet<>(listTwo));
    }

    public <T, V> boolean verifyTwoListSizeEquals(List<T> arr1, List<V> arr2) {

        return arr1.size() == arr2.size();
    }
}

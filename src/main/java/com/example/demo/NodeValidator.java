package com.example.demo;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class NodeValidator {
//
////    public static List<String> findDuplicateName(final List<Map<String, Object>> allTrees,final String scontainername) {
////
////        final Map<String, Integer> nameCount = new HashMap<>();
////
////        for (final Map<String, Object> tree : allTrees) {
////        	
////        	    	
////            collectNames(tree, nameCount);
////        }
////
////        final List<String> duplicates = new ArrayList<>();
////
////        for (final Map.Entry<String, Integer> entry : nameCount.entrySet()) {
////            if (entry.getValue() > 1) {
////                duplicates.add(entry.getKey());
////            }
////        }
////
////        return duplicates;
////    }
////
//
//	public static List<String> findDuplicateName(final List<Map<String, Object>> allTrees,
//			final String scontainername) {
//
//		final Map<String, Integer> nameCount = new HashMap<>();
//
//		for (final Map<String, Object> wrapper : allTrees) {
//
//			final List<Map<String, Object>> roots = (List<Map<String, Object>>) wrapper.get("tree");
//
//			if (roots != null) {
//				for (final Map<String, Object> root : roots) {
//					collectNames(root, nameCount);
//				}
//			}
//		}
//
//		final List<String> duplicates = new ArrayList<>();
//
//		for (final Map.Entry<String, Integer> entry : nameCount.entrySet()) {
//			if (entry.getValue() > 1) {
//				duplicates.add(entry.getKey());
//			}
//		}
//
//		return duplicates;
//	}
////	
////	
//
////	private static void collectNames(final Map<String, Object> node, final Map<String, Integer> nameCount) {
////
////		final String name = (String) node.get("displayName");
////
////		System.out.println("Node" + node);
////
////		if (name != null) {
////			nameCount.put(name, nameCount.getOrDefault(name, 0) + 1);
////		}
////
////		final List<?> children = (List<?>) node.get("children");
////
////		if (children != null) {
////			for (final Object child : children) {
////				collectNames((Map<String, Object>) child, nameCount);
////			}
////		}
////	}
//
//	
//
//	
//	private static void collectNames(final Map<String, Object> node, final Map<String, Integer> nameCount) {
//
//		final String name = (String) node.get("displayName");
//
//		if (name != null) {
//			nameCount.put(name, nameCount.getOrDefault(name, 0) + 1);
//		}
//
//		final List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
//
//		if (children != null) {
//			for (final Map<String, Object> child : children) {
//				collectNames(child, nameCount);
//			}
//		}
//	}
//
//}

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NodeValidator {

    public static List<String> findDuplicateName(
            final List<Map<String, Object>> allTrees,
            final String scontainername) {

        final Map<String, Integer> nameCount = new HashMap<>();

        // Step 1: Count all displayNames from all trees
        for (final Map<String, Object> wrapper : allTrees) {

            final List<Map<String, Object>> roots =
                    (List<Map<String, Object>>) wrapper.get("tree");

            if (roots != null) {
                for (final Map<String, Object> root : roots) {
                    collectNames(root, nameCount);
                }
            }
        }


        // Step 2: Find current tree
        Map<String, Object> currentTree = null;

        for (final Map<String, Object> wrapper : allTrees) {

            final String containerName =
                    (String) wrapper.get("containerName");

            if (scontainername.equals(containerName)) {
                currentTree = wrapper;
                break;
            }
        }


        // If current tree not found, no duplicate validation
        if (currentTree == null) {
            return Collections.emptyList();
        }


        // Step 3: Get all names from current tree
        final Set<String> currentTreeNames = new HashSet<>();

        final List<Map<String, Object>> roots =
                (List<Map<String, Object>>) currentTree.get("tree");

        if (roots != null) {
            for (final Map<String, Object> root : roots) {
                collectNames(root, currentTreeNames);
            }
        }


        // Step 4: Return only duplicates from current tree
        final List<String> duplicates = new ArrayList<>();

        for (final String name : currentTreeNames) {

            if (nameCount.getOrDefault(name, 0) > 1) {
                duplicates.add(name);
            }
        }


        return duplicates;
    }



    // Used for counting duplicate names
    private static void collectNames(
            final Map<String, Object> node,
            final Map<String, Integer> nameCount) {

        final String name =
                (String) node.get("displayName");

        if (name != null) {
            nameCount.put(
                    name,
                    nameCount.getOrDefault(name, 0) + 1
            );
        }


        final List<Map<String, Object>> children =
                (List<Map<String, Object>>) node.get("children");


        if (children != null) {

            for (final Map<String, Object> child : children) {
                collectNames(child, nameCount);
            }
        }
    }



    // Used for collecting current tree names
    private static void collectNames(
            final Map<String, Object> node,
            final Set<String> names) {

        final String name =
                (String) node.get("displayName");

        if (name != null) {
            names.add(name);
        }


        final List<Map<String, Object>> children =
                (List<Map<String, Object>>) node.get("children");


        if (children != null) {

            for (final Map<String, Object> child : children) {
                collectNames(child, names);
            }
        }
    }
}
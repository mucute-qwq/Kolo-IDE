cmake_minimum_required(VERSION 3.22.1)
project("merminal")

add_library(merminal
        SHARED
        merminal.cpp)

target_link_libraries(merminal)
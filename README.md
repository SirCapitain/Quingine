# Quingine
A 3D engine, in java, made from scratch.

Latest version: something. I dunno. Just check.

This 3D engine uses both Quaternions and Euler to determine rotation of points in space,
and the use of vector to determine what to draw and lighting.
However, due to quite limited knowledge of everything,
this code is poorly optimized.

*PLEASE NOTE*
This program is 100% CPU based. I could install a library to
make it run on a GPU buuuuuut.... I don't wanna.

If downloaded you may notice a terrible naming scheme.
Deal with it. Unless you don't want to.

*HOW TO USE*
Open this project in a java coding software. To make your first 3D object you first need to make a Quindow.
After you make a Quindow you need to add a Quicture to it so it can draw stuff. Next, create a 3D object 
using one of the pre-made shapes or using a .obj (further down explains how to make a .obj) file with only points and faces only. Finally, add that to 
the Quicture and marvel of the pretty object. If you want more options, make a Quworld, set it to the Quicture, then add your quobjects to the
Quworld instead.

*3D Pre-Made Objects Available*
-Quectangular Quism (Rectangular Prism)
-Qube (Cube)
-Qyriamid (Pyramid)
-Few blender made .obj files

*Supports blender made .obj*
To use this cool feature, make any object in blender. Then export it as a .obj, triangulate the mesh, and have normals off. Finally, put the .obj into the
object folder in resources and call the file by creating a quobject. AND DONE! That is all you have to do! 

*Supports simple physics*
Particles: Forces applied to particles and collision of particles

CHANGES MAYBE TO COME! (This is if I don't give up on this project)
-Math optimizations
-Smoother lighting
-GPU Acceleration (nah)
-Looking around using the mouse
-Antialiasing (just bump of the resolution)
-Even worse naming scheme
-Pain

Take this code if you want. I don't care.

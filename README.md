# Description

This is a software ray-marching renderer I made primarily to learn java. It has a bunch of stuff that I played around with, so its a bit bloated. This is a continuation of a previous [renderer I made in C](https://github.com/martzin23/bitmap-export-raymarcher). Created in March 2024.

The main features are:
- Ray marching rendering with a movable camera with K&M
- Editing bodies in the scene
- Multiple shading modes
- Indirect lighting with ray tracing
- PBR rendering (metallic, roughness)
- Rendering a 3D image as cloud volume
- Basic gravity simulation (only applies to spheres)
- Importing a obj file (not optimized)
- Displaying a heightmap on a plane

# Usage

## Controls

- Basic
    - `WASDQE` - movement
    - `Scroll` - increase/decrease movement speed
    - `Up/Down arrow` - increase/decrease resolution
    - `Right/Left arrow` - increase/decrease tracing quality
- Body editing
    - `LMB` - select body
    - `RMB` - deselect body
    - `MMB` - hold to grab selected body
    - `T/Z/U + scroll` - edit body position
    - `G/H/J + scroll` - edit body size
    - `B/N/M + scroll` - edit body color
    - `F + scroll` - edit body emission (can be negative for darkness sources)
- Body managment
    - `I` - create sphere
    - `O` - create box
    - `K` - create elipse
    - `L` - create sphere with velocity marker
    - `Backspace` - delete selected body
- Shading modes
    - `1` - raymarching lit
    - `2` - raytracing lit (increasing tracing quality adds indirect lighting)
    - `3` - normals
    - `4` - normals alternative 
    - `5` - volumes
    - `6-9` - blank
- Simulation
    - `Space` - pause and unpause body movement
    - `Shift` - push selected body
    - `Alt` - pull selected body

## Configuration

A lot of stuff is only configurable by editing constants in the code or by commenting/uncommenting lines. Some of the notable classes to edit are: 
- `Bodies.initBodyCollection()` - contains some presets for scenes
- `Image.initImages()` - paths of images the program uses
    - `height` is used for displacing the surface of a DisplacePlane body
    - `volume` is a 3D image used for the volume shading mode
- `Shader` - contains important constants for raymarching and shading

# Some renders

## Reflections and phong lighting
![1712049584943.jpg](/renders/1712049584943.jpg)

## Inported obj model
![1713803719975.jpg](/renders/1713803719975.jpg)

## Heightmap displaced plane
![1752066514355.jpg](/renders/1752066514355.jpg)

## Heightmap with textures
![1712037077147.jpg](/renders/1712037077147.jpg)

## Volume from 3D image
![1752062481352.jpg](/renders/1752062481352.jpg)

## Indirect lighting
![1713808181099.jpg](/renders/1713808181099.jpg)

## PBR
![1714635593069.jpg](/renders/1714635593069.jpg)
